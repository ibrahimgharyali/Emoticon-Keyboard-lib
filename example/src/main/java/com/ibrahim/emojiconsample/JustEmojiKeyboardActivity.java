package com.ibrahim.emojiconsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import github.ibrahimgharyali.emojicon.EmojiconEditText;
import github.ibrahimgharyali.emojicon.EmojiconGridView;
import github.ibrahimgharyali.emojicon.EmojiconTextView;
import github.ibrahimgharyali.emojicon.EmojiconsPopup;
import github.ibrahimgharyali.emojicon.emoji.Emojicon;

public class JustEmojiKeyboardActivity extends AppCompatActivity {

    private EmojiconsPopup emojiconsPopup;
    private EmojiconEditText emojiconTextView;
    private FrameLayout keyBoardAnchor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_just_emoji_keyboard);
        emojiconTextView = (EmojiconEditText) findViewById(R.id.emoji_textview);
        keyBoardAnchor = (FrameLayout)findViewById(R.id.emojicons);
        showEmoticonKeyboard(true);
    }

    private void showEmoticonKeyboard(boolean b) {
        // Give the topmost view of your activity layout hierarchy. This will be used to measure soft keyboard height
        emojiconsPopup = new EmojiconsPopup(keyBoardAnchor, this);
        //Will automatically set size according to the soft keyboard size
//        emojiconsPopup.setSizeForSoftKeyboard();

        //If the emoji emojiconsPopup is dismissed, change emojiButton to smiley icon
        emojiconsPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
            }
        });

        //If the text keyboard closes, also dismiss the emoji emojiconsPopup
        emojiconsPopup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {

            @Override
            public void onKeyboardOpen(int keyBoardHeight) {

            }

            @Override
            public void onKeyboardClose() {
                if(emojiconsPopup.isShowing())
                    emojiconsPopup.dismiss();
            }
        });

        //On emoji clicked, add it to edittext
        emojiconsPopup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {

            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                if (emojiconTextView == null|| emojicon == null) {
                    return;
                }
                setEmoji(emojicon);
            }
        });

        //On backspace clicked, emulate the KEYCODE_DEL key event
        emojiconsPopup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

            @Override
            public void onEmojiconBackspaceClicked(View v) {
                KeyEvent event = new KeyEvent(
                        0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                emojiconTextView.dispatchKeyEvent(event);
//                removeEmoji();
            }
        });

        ViewTreeObserver vto = keyBoardAnchor.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                keyBoardAnchor.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                emojiconsPopup.showAtBottom();

            }
        });


    }
    public void setEmoji(Emojicon emojicon) {
        emojiconTextView.append(emojicon.getEmoji());
    }
    public void removeEmoji() {
            emojiconTextView.setText(emojiconTextView.getText().toString().substring(0, emojiconTextView.getText().length()-1));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
