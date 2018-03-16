package com.dudar.colorfulmind;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import com.dudar.colorfulmind.colorlogic.ColorLogicItem;
import com.dudar.colorfulmind.colorlogic.ColorLogicPresenter;
import com.dudar.colorfulmind.colorlogic.ColorsLogicHelpActivity;
import com.dudar.colorfulmind.colorlogic.MyColorButton;

public class ColorslogicActivity extends AppCompatActivity implements View.OnClickListener, ColorLogicContract.View {

    int[] baseColors;
    int chosenColor;
    MyColorButton imgBtnColor1, imgBtnColor2, imgBtnColor3, imgBtnColor4;
    ImageView imgSecretColor1, imgSecretColor2, imgSecretColor3, imgSecretColor4;
    ListView listView;
    TextView textViewAttemptNumber;
    ColorLogicAdapter itemsAdapter;
    ColorLogicHistory history;

    ColorLogicContract.Presenter presenter;

    static final int GAME_LEVEL_EASY = 0;
    static final int GAME_LEVEL_MIDDLE = 1;
    static final int GAME_LEVEL_HARD = 2;
    static final int GAME_LEVEL_VERYHARD = 3;
    static final int GAME_LEVEL_CRAZY = 4;

    static final String GAME_LEVEL_KEY = "color_logic_game_level";
    static final String GAME_DUPLICATION_KEY = "color_logic_game_duplication";

    static final int MENU_ITEM_GAME_LEVEL = 0;
    static final int MENU_ITEM_DUPLICATION = 1;

    int gameLevel;
    int numberOfColors;
    int numberOfRemainingMoves;

    Boolean isDuplicationAllowed;
    static final Boolean DUPLICATION_NOT_ALLOWED = false;

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
        presenter.setSecretColors(baseColors, numberOfColors,isDuplicationAllowed);

        history = ColorLogicHistory.getHistoryInstance();
        itemsAdapter = new ColorLogicAdapter(this, history.getHistoryItems());
        listView.setAdapter(itemsAdapter);

        ///Delete this call later
        showSecretColours();

    }

    private void initViews() {
        imgBtnColor1 = (MyColorButton) findViewById(R.id.btnPickColor1);
        imgBtnColor2 = (MyColorButton) findViewById(R.id.btnPickColor2);
        imgBtnColor3 = (MyColorButton) findViewById(R.id.btnPickColor3);
        imgBtnColor4 = (MyColorButton) findViewById(R.id.btnPickColor4);

        listView = (ListView) findViewById(R.id.listView);

        imgSecretColor1 = (ImageView) findViewById(R.id.imageViewSecretColor1);
        imgSecretColor2 = (ImageView) findViewById(R.id.imageViewSecretColor2);
        imgSecretColor3 = (ImageView) findViewById(R.id.imageViewSecretColor3);
        imgSecretColor4 = (ImageView) findViewById(R.id.imageViewSecretColor4);

        textViewAttemptNumber = (TextView) findViewById(R.id.attemptNumberTextView);
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
            case GAME_LEVEL_VERYHARD:
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
                }
                else {
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
                    gameLevel = GAME_LEVEL_VERYHARD;
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
        //Log.i("coloractivity", "in onrestart method");
        history.deleteHistory();
        itemsAdapter.notifyDataSetChanged();

        fetchParameters();
        presenter.setSecretColors(baseColors, numberOfColors, isDuplicationAllowed);

        imgBtnColor1.refreshColor();
        imgBtnColor2.refreshColor();
        imgBtnColor3.refreshColor();
        imgBtnColor4.refreshColor();

        showSecretColours();
    }


    private void onApproveAttemptBtnClick() {
        if (allColorsAreSet()) {
            if (allColorsAreSetCorrectly()) {
                presenter.makePlayerStep(imgBtnColor1.getBgColorId(), imgBtnColor2.getBgColorId(),
                        imgBtnColor3.getBgColorId(), imgBtnColor4.getBgColorId());
                updateNumberOfMoves();
            } else {
                Snackbar mySnack = Snackbar.make(findViewById(R.id.colorLogicID), "All colors should be different", Snackbar.LENGTH_SHORT);
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
        dialog.setTitle(R.string.dialog_title);
        dialog.setCancelable(true);

        ImageButton dialogRedBtn = (ImageButton) dialog.findViewById(R.id.btnRedColor);
        ImageButton dialogYellowBtn = (ImageButton) dialog.findViewById(R.id.btnYellowColor);
        ImageButton dialogGreenBtn = (ImageButton) dialog.findViewById(R.id.btnGreenColor);
        ImageButton dialogBlueBtn = (ImageButton) dialog.findViewById(R.id.btnBlueColor);
        ImageButton dialogPurpleBtn = (ImageButton) dialog.findViewById(R.id.btnPurpleColor);
        ImageButton dialogCyanBtn = (ImageButton) dialog.findViewById(R.id.btnCyanColor);

        ImageButton dialogOrangeBtn = (ImageButton) dialog.findViewById(R.id.btnOrangeColor);
        ImageButton dialogBrownBtn = (ImageButton) dialog.findViewById(R.id.btnBrownColor);
        ImageButton dialogGreyBtn = (ImageButton) dialog.findViewById(R.id.btnGreyColor);
        ImageButton dialogBlackBtn = (ImageButton) dialog.findViewById(R.id.btnBlackColor);


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
            case GAME_LEVEL_VERYHARD: {
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
        dialogWindow.setTitle("Choose color");

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
            Dialog dialog = new Dialog(this);
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

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(GAME_LEVEL_KEY, gameLevel);
        editor.putBoolean(GAME_DUPLICATION_KEY, isDuplicationAllowed);
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        history.deleteHistory();
    }
}
