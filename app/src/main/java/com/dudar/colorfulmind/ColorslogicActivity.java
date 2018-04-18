package com.dudar.colorfulmind;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dudar.colorfulmind.colorlogic.ColorLogicAdapter;
import com.dudar.colorfulmind.colorlogic.ColorLogicContract;
import com.dudar.colorfulmind.colorlogic.ColorLogicHistory;
import com.dudar.colorfulmind.colorlogic.ColorLogicHistoryItem;
import com.dudar.colorfulmind.colorlogic.ColorLogicItem;
import com.dudar.colorfulmind.colorlogic.ColorLogicPresenter;
import com.dudar.colorfulmind.colorlogic.ColorsLogicHelpActivity;
import com.dudar.colorfulmind.colorlogic.MyColorButton;

import java.util.ArrayList;

public class ColorslogicActivity extends AppCompatActivity implements View.OnClickListener, ColorLogicContract.View {

    int[] baseColors;
    int chosenColor;
    MyColorButton imgBtnColor1, imgBtnColor2, imgBtnColor3, imgBtnColor4;
    ImageButton btnApproveAttempt;
    ImageView imgSecretColor1, imgSecretColor2, imgSecretColor3, imgSecretColor4;
    ListView listView;
    TextView textViewAttemptNumber;
    ColorLogicAdapter itemsAdapter;
    ColorLogicHistory history;

    ColorLogicContract.Presenter presenter;

    static final int GAME_LEVEL_EASY = 0;
    static final int GAME_LEVEL_MIDDLE = 1;
    static final int GAME_LEVEL_HARD = 2;
    static final int GAME_LEVEL_VERY_HARD = 3;
    static final int GAME_LEVEL_CRAZY = 4;

    static final String GAME_LEVEL_KEY = "color_logic_game_level";
    static final String GAME_DUPLICATION_KEY = "color_logic_game_duplication";

    static final int MENU_ITEM_GAME_LEVEL = 1;
    static final int MENU_ITEM_DUPLICATION = 2;

    int gameLevel;
    int numberOfColors;
    int numberOfRemainingMoves;

    Boolean isDuplicationAllowed;
    static final Boolean DUPLICATION_NOT_ALLOWED = false;

    static final int GAME_OVER_WIN = 1;
    static final int GAME_OVER_LOST = 2;

    static final String STATE_ITEM_REMAINING_MOVES_KEY = "state_item_remaining_moves_key";
    static final String STATE_ITEM_SECRET_COLORS_KEY = "state_item_secret_colors_key";
    static final String STATE_ITEM_GUESS_COLORS_KEY = "state_item_guess_colors_key";
    static final String STATE_ITEM_HISTORY_KEY = "state_item_history_key";

    int[] restoredSecretColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorslogic);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        gameLevel = prefs.getInt(GAME_LEVEL_KEY, GAME_LEVEL_CRAZY);
        isDuplicationAllowed = prefs.getBoolean(GAME_DUPLICATION_KEY, DUPLICATION_NOT_ALLOWED);

        initViews();
        setListeners();

        fetchParameters();

        presenter = new ColorLogicPresenter(this);

        history = ColorLogicHistory.getHistoryInstance();

        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
            presenter.copySecretColors(restoredSecretColors);
        } else {
            presenter.setSecretColors(baseColors, numberOfColors, isDuplicationAllowed);
        }

        itemsAdapter = new ColorLogicAdapter(this, history.getHistoryItems());
        listView.setAdapter(itemsAdapter);
    }

    private void restoreState(Bundle savedInstanceState) {

        numberOfRemainingMoves = savedInstanceState.getInt(STATE_ITEM_REMAINING_MOVES_KEY);
        textViewAttemptNumber.setText(String.format("%d", numberOfRemainingMoves));

        restoredSecretColors = savedInstanceState.getIntArray(STATE_ITEM_SECRET_COLORS_KEY);

        int[] restoredGuessColors = savedInstanceState.getIntArray(STATE_ITEM_GUESS_COLORS_KEY);
        if (restoredGuessColors != null) {
            imgBtnColor1.setBackgroundResource(restoredGuessColors[0]);
            imgBtnColor2.setBackgroundResource(restoredGuessColors[1]);
            imgBtnColor3.setBackgroundResource(restoredGuessColors[2]);
            imgBtnColor4.setBackgroundResource(restoredGuessColors[3]);
        }

        ArrayList<ColorLogicHistoryItem> recoveredHistory = savedInstanceState.getParcelableArrayList(STATE_ITEM_HISTORY_KEY);
        history.copyHistory(recoveredHistory);
        //itemsAdapter.notifyDataSetChanged();
        //listView.setSelection(listView.getAdapter().getCount() - 1);
    }

    private void initViews() {
        imgBtnColor1 = findViewById(R.id.btnPickColor1);
        imgBtnColor2 = findViewById(R.id.btnPickColor2);
        imgBtnColor3 = findViewById(R.id.btnPickColor3);
        imgBtnColor4 = findViewById(R.id.btnPickColor4);

        listView = findViewById(R.id.listView);

        imgSecretColor1 = findViewById(R.id.imageViewSecretColor1);
        imgSecretColor2 = findViewById(R.id.imageViewSecretColor2);
        imgSecretColor3 = findViewById(R.id.imageViewSecretColor3);
        imgSecretColor4 = findViewById(R.id.imageViewSecretColor4);

        textViewAttemptNumber = findViewById(R.id.attemptNumberTextView);

        btnApproveAttempt = findViewById(R.id.btnApproveAttempt);
    }

    private void setListeners() {

        imgBtnColor1.setOnClickListener(this);
        imgBtnColor2.setOnClickListener(this);
        imgBtnColor3.setOnClickListener(this);
        imgBtnColor4.setOnClickListener(this);

        findViewById(R.id.btnApproveAttempt).setOnClickListener(this);
        findViewById(R.id.btnRestart).setOnClickListener(this);
    }

    private void fetchParameters() {

        Resources res = getResources();
        TypedArray ta;

        switch (gameLevel) {
            case GAME_LEVEL_EASY:
                numberOfRemainingMoves = 20;

                ta = res.obtainTypedArray(R.array.logic_colors_array);
                baseColors = new int[ta.length()];
                for (int i = 0; i < ta.length(); i++) {
                    baseColors[i] = ta.getResourceId(i, 0);
                }
                numberOfColors = ta.length();
                ta.recycle();
                break;
            case GAME_LEVEL_MIDDLE:
                numberOfRemainingMoves = 10;

                ta = res.obtainTypedArray(R.array.logic_colors_array);
                baseColors = new int[ta.length()];
                for (int i = 0; i < ta.length(); i++) {
                    baseColors[i] = ta.getResourceId(i, 0);
                }
                numberOfColors = ta.length();
                ta.recycle();
                break;
            case GAME_LEVEL_HARD:
                numberOfRemainingMoves = 15;

                ta = res.obtainTypedArray(R.array.logic_colors_array_big);
                baseColors = new int[ta.length()];
                for (int i = 0; i < ta.length(); i++) {
                    baseColors[i] = ta.getResourceId(i, 0);
                }

                numberOfColors = ta.length();
                ta.recycle();
                break;
            case GAME_LEVEL_VERY_HARD:
                numberOfRemainingMoves = 10;

                ta = res.obtainTypedArray(R.array.logic_colors_array_big);
                baseColors = new int[ta.length()];
                for (int i = 0; i < ta.length(); i++) {
                    baseColors[i] = ta.getResourceId(i, 0);
                }

                numberOfColors = ta.length();
                ta.recycle();
                break;

            case GAME_LEVEL_CRAZY:
                numberOfRemainingMoves = 10;

                ta = res.obtainTypedArray(R.array.logic_colors_array_crazy);
                baseColors = new int[ta.length()];
                for (int i = 0; i < ta.length(); i++) {
                    baseColors[i] = ta.getResourceId(i, 0);
                }
                numberOfColors = ta.length();
                ta.recycle();
                break;
        }

        textViewAttemptNumber.setText(String.format("%d", numberOfRemainingMoves));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_colorlogic, menu);
        menu.getItem(MENU_ITEM_GAME_LEVEL).getSubMenu().getItem(gameLevel).setChecked(true);
        menu.getItem(MENU_ITEM_DUPLICATION).setChecked(isDuplicationAllowed);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.action_help:
                startActivity(new Intent(this, ColorsLogicHelpActivity.class));
                return true;
            case R.id.action_duplicate:
                if (item.isChecked()) {
                    item.setChecked(false);
                    isDuplicationAllowed = false;
                } else {
                    item.setChecked(true);
                    isDuplicationAllowed = true;
                }
                onRestartBtnClick();
                return true;
            case R.id.menu_level_easy:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    gameLevel = GAME_LEVEL_EASY;
                    onRestartBtnClick();
                }
                return true;
            case R.id.menu_level_middle:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    gameLevel = GAME_LEVEL_MIDDLE;
                    onRestartBtnClick();
                }
                return true;
            case R.id.menu_level_hard:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    gameLevel = GAME_LEVEL_HARD;
                    onRestartBtnClick();
                }
                return true;
            case R.id.menu_level_veryhard:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    gameLevel = GAME_LEVEL_VERY_HARD;
                    onRestartBtnClick();
                }
                return true;
            case R.id.menu_level_crazy:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    gameLevel = GAME_LEVEL_CRAZY;
                    onRestartBtnClick();
                }
                return true;
            default:
                return true;
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnPickColor1:
                chooseNewColor(this, imgBtnColor1);
                break;
            case R.id.btnPickColor2:
                chooseNewColor(this, imgBtnColor2);
                break;
            case R.id.btnPickColor3:
                chooseNewColor(this, imgBtnColor3);
                break;
            case R.id.btnPickColor4:
                chooseNewColor(this, imgBtnColor4);
                break;
            case R.id.btnApproveAttempt:
                onApproveAttemptBtnClick();
                break;
            case R.id.btnRestart:
                onRestartBtnClick();
                break;
        }
    }

    private void onRestartBtnClick() {
        history.deleteHistory();
        itemsAdapter.notifyDataSetChanged();

        fetchParameters();
        presenter.setSecretColors(baseColors, numberOfColors, isDuplicationAllowed);

        imgBtnColor1.refreshColor();
        imgBtnColor2.refreshColor();
        imgBtnColor3.refreshColor();
        imgBtnColor4.refreshColor();

        imgSecretColor1.setBackgroundResource(android.R.drawable.ic_lock_lock);
        imgSecretColor1.setImageResource(R.drawable.plainframe);
        imgSecretColor2.setBackgroundResource(android.R.drawable.ic_lock_lock);
        imgSecretColor2.setImageResource(R.drawable.plainframe);
        imgSecretColor3.setBackgroundResource(android.R.drawable.ic_lock_lock);
        imgSecretColor3.setImageResource(R.drawable.plainframe);
        imgSecretColor4.setBackgroundResource(android.R.drawable.ic_lock_lock);
        imgSecretColor4.setImageResource(R.drawable.plainframe);

        btnApproveAttempt.setEnabled(true);
    }


    private void onApproveAttemptBtnClick() {
        if (allColorsAreSet()) {
            if (allColorsAreSetCorrectly()) {
                updateNumberOfMoves();
                presenter.makePlayerStep(imgBtnColor1.getBgColorId(), imgBtnColor2.getBgColorId(),
                        imgBtnColor3.getBgColorId(), imgBtnColor4.getBgColorId());
            } else {
                Snackbar mySnack = Snackbar.make(findViewById(R.id.colorLogicID), R.string.colors_no_duplication, Snackbar.LENGTH_SHORT);
                mySnack.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mySnack.show();
            }
        } else {
            Snackbar mySnack = Snackbar.make(findViewById(R.id.colorLogicID), R.string.colors_not_set, Snackbar.LENGTH_SHORT);
            mySnack.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            mySnack.show();
        }

    }

    private boolean allColorsAreSet() {
        return (imgBtnColor1.getBgColorId() > 0 && imgBtnColor2.getBgColorId() > 0 &&
                imgBtnColor3.getBgColorId() > 0 && imgBtnColor4.getBgColorId() > 0);
    }

    private boolean allColorsAreSetCorrectly() {
        int c1 = imgBtnColor1.getBgColorId();
        int c2 = imgBtnColor2.getBgColorId();
        int c3 = imgBtnColor3.getBgColorId();
        int c4 = imgBtnColor4.getBgColorId();
        return isDuplicationAllowed || ((c1 != c2) && (c1 != c3) && (c1 != c4) && (c2 != c3) && (c2 != c4) && (c3 != c4));
    }

    private void updateNumberOfMoves() {
        numberOfRemainingMoves--;
        textViewAttemptNumber.setText(String.format("%d", numberOfRemainingMoves));
    }

    private void chooseNewColor(Context context, final ImageButton imgToUpdate) {


        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.pick_color_dialog);
        dialog.setCancelable(true);

        ImageButton dialogRedBtn = dialog.findViewById(R.id.btnRedColor);
        ImageButton dialogYellowBtn = dialog.findViewById(R.id.btnYellowColor);
        ImageButton dialogGreenBtn = dialog.findViewById(R.id.btnGreenColor);
        ImageButton dialogBlueBtn = dialog.findViewById(R.id.btnBlueColor);
        ImageButton dialogPurpleBtn = dialog.findViewById(R.id.btnPurpleColor);
        ImageButton dialogCyanBtn = dialog.findViewById(R.id.btnCyanColor);

        ImageButton dialogOrangeBtn = dialog.findViewById(R.id.btnOrangeColor);
        ImageButton dialogBrownBtn = dialog.findViewById(R.id.btnBrownColor);
        ImageButton dialogGreyBtn = dialog.findViewById(R.id.btnGreyColor);
        ImageButton dialogBlackBtn = dialog.findViewById(R.id.btnBlackColor);


        dialogRedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenColor = R.color.red;
                dialog.dismiss();
                updateBtnWithNewColor(imgToUpdate, chosenColor);
            }
        });

        dialogYellowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenColor = R.color.yellow;
                dialog.dismiss();
                updateBtnWithNewColor(imgToUpdate, chosenColor);
            }
        });


        dialogGreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenColor = R.color.green;
                dialog.dismiss();
                updateBtnWithNewColor(imgToUpdate, chosenColor);
            }
        });

        dialogBlueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenColor = R.color.blue;
                dialog.dismiss();
                updateBtnWithNewColor(imgToUpdate, chosenColor);
            }
        });

        dialogPurpleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenColor = R.color.purple;
                dialog.dismiss();
                updateBtnWithNewColor(imgToUpdate, chosenColor);

            }
        });

        dialogCyanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenColor = R.color.cyan;
                dialog.dismiss();
                updateBtnWithNewColor(imgToUpdate, chosenColor);
            }
        });

        /////////////
        dialogOrangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenColor = R.color.orange;
                dialog.dismiss();
                updateBtnWithNewColor(imgToUpdate, chosenColor);
            }
        });

        dialogBrownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenColor = R.color.brown;
                dialog.dismiss();
                updateBtnWithNewColor(imgToUpdate, chosenColor);
            }
        });

        dialogGreyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenColor = R.color.grey;
                dialog.dismiss();
                updateBtnWithNewColor(imgToUpdate, chosenColor);
            }
        });

        dialogBlackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenColor = R.color.black;
                dialog.dismiss();
                updateBtnWithNewColor(imgToUpdate, chosenColor);
            }
        });

        switch (gameLevel) {
            case GAME_LEVEL_EASY:
            case GAME_LEVEL_MIDDLE: {
                dialogOrangeBtn.setVisibility(View.GONE);
                dialogBrownBtn.setVisibility(View.GONE);
                dialogGreyBtn.setVisibility(View.GONE);
                dialogBlackBtn.setVisibility(View.GONE);
                break;
            }
            case GAME_LEVEL_HARD:
            case GAME_LEVEL_VERY_HARD: {
                dialogGreyBtn.setVisibility(View.GONE);
                dialogBlackBtn.setVisibility(View.GONE);
                break;
            }
        }

        changeDialogPosition(dialog);

        dialog.show();
    }

    private void changeDialogPosition(Dialog dialog) {
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM | Gravity.LEFT);

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.85);
        //lp.horizontalMargin=5.0f;
        //lp.horizontalWeight = 80;

        dialogWindow.setAttributes(lp);
    }

    private void updateBtnWithNewColor(ImageButton imgToUpdate, int chosenColor) {
        imgToUpdate.setBackgroundResource(chosenColor);
    }


    @Override
    public void updateHistoryView(int colBull) {
        itemsAdapter.notifyDataSetChanged();
        listView.setSelection(listView.getAdapter().getCount() - 1);
        if (colBull == 4) {
            showGameOver(GAME_OVER_WIN);
        } else if (numberOfRemainingMoves == 0) {
            showGameOver(GAME_OVER_LOST);
        }
    }

    void showSecretColours() {
        ColorLogicItem colors = presenter.getSecretColors();

        imgSecretColor1.setImageResource(R.drawable.plainframe);
        imgSecretColor1.setBackgroundResource(colors.getColor1());

        imgSecretColor2.setImageResource(R.drawable.plainframe);
        imgSecretColor2.setBackgroundResource(colors.getColor2());

        imgSecretColor3.setImageResource(R.drawable.plainframe);
        imgSecretColor3.setBackgroundResource(colors.getColor3());

        imgSecretColor4.setImageResource(R.drawable.plainframe);
        imgSecretColor4.setBackgroundResource(colors.getColor4());
    }

    void showGameOver(int gameOverType) {

        showSecretColours();
        btnApproveAttempt.setEnabled(false);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this)
                .setPositiveButton(R.string.answer_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(ColorslogicActivity.this, "Yes", Toast.LENGTH_SHORT).show();
                        onRestartBtnClick();
                    }
                })
                .setNegativeButton(R.string.answer_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(ColorslogicActivity.this, "No", Toast.LENGTH_SHORT).show();
                    }
                });

        View dialogView = getLayoutInflater().inflate(R.layout.gameover_dialog, null);
        TextView gameOverTextView = dialogView.findViewById(R.id.gameoverTextView);
        ImageView gameOverImage = dialogView.findViewById(R.id.gameoverImageView);
        switch (gameOverType) {
            case GAME_OVER_WIN:
                gameOverTextView.setText(R.string.win_text);
                gameOverImage.setBackgroundResource(R.drawable.bull_message);
                break;
            case GAME_OVER_LOST:
                gameOverTextView.setText(R.string.looser_text);
                gameOverImage.setBackgroundResource(R.drawable.cow_message);
                break;
        }

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(GAME_LEVEL_KEY, gameLevel);
        editor.putBoolean(GAME_DUPLICATION_KEY, isDuplicationAllowed);
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_ITEM_REMAINING_MOVES_KEY, numberOfRemainingMoves);
        int[] secretColors = {presenter.getSecretColors().getColor1(), presenter.getSecretColors().getColor2(),
                presenter.getSecretColors().getColor3(), presenter.getSecretColors().getColor4()};
        outState.putIntArray(STATE_ITEM_SECRET_COLORS_KEY, secretColors);

        int[] guessColors = {imgBtnColor1.getBgColorId(), imgBtnColor2.getBgColorId(),
                imgBtnColor3.getBgColorId(), imgBtnColor4.getBgColorId()};
        outState.putIntArray(STATE_ITEM_GUESS_COLORS_KEY, guessColors);

        outState.putParcelableArrayList(STATE_ITEM_HISTORY_KEY, history.getHistoryItems());
        outState.putParcelableArrayList("my_history", history.getHistoryItems());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        history.deleteHistory();
    }

}
