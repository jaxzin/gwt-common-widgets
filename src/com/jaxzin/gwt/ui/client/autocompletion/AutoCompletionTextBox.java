package com.jaxzin.gwt.ui.client.autocompletion;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.core.client.GWT;

/**
 * <pre>
 * Date: Nov 1, 2006
 * Time: 9:29:57 AM
 * </pre>
 *
 * @author <a href="mailto:Brian-R.Jackson@ubs.com">Brian-R Jackson</a>
 */
public class AutoCompletionTextBox extends TextBox {
    private AutoCompletionMatcherServiceAsync matcher;
    private AutoCompletionResultPanel resultPanel;
    private String userEntered = "";

    public AutoCompletionTextBox(final AutoCompletionMatcherServiceAsync matcher) {
        super();
        this.matcher = matcher;
        resultPanel = new AutoCompletionResultPanel();
        resultPanel.addClickListener(
                new ClickListener() {
                    public void onClick(Widget sender) {
                        updateTextBasedOnSelection();
                        setCursorPos(getText().length());
                        userEntered = getText();
                        hideResult();
                    }
                }
        );

        addKeyboardListener(new DefaultKeyboardListener());

        addFocusListener(
                new FocusListenerAdapter() {
                    public void onLostFocus(Widget sender) {
                        setText(userEntered);
                    }
                }
        );

        addStyleName("cw-AutoCompletionTextBox");

    }

    protected void showResult(MatcherResult result) {
        resultPanel.setResult(result);
        resultPanel.setSelectedIndex(0);

        // Set the resultPanel width and position
        int left = getAbsoluteLeft();
        int top = getAbsoluteTop() + getOffsetHeight();
        int width = getOffsetWidth();
        resultPanel.setPopupPosition(left, top);
        resultPanel.setWidth(width+"px");

        resultPanel.show();
        updateTextBasedOnSelection();
    }

    protected void hideResult() {
        resultPanel.hide();
    }

    protected void selectNext() {
        resultPanel.show();
        resultPanel.setSelectedIndex(Math.min(resultPanel.getResult().getMatches().size()-1, resultPanel.getSelectedIndex()+1));
        updateTextBasedOnSelection();
    }

    protected void selectPrevious() {
        resultPanel.show();
        resultPanel.setSelectedIndex(Math.max(0,resultPanel.getSelectedIndex()-1));
        updateTextBasedOnSelection();
    }

    private void updateTextBasedOnSelection() {
        if(!resultPanel.getResult().getMatches().isEmpty()) {
            // TODO: I suspect there is a race-condition here...
            int cursor = getCursorPos();
            String text = (String) resultPanel.getResult().getMatches().get(resultPanel.getSelectedIndex());
            setText(text);
            setSelectionRange(cursor, text.length() - cursor);
            GWT.log("text: '"+text+"'; pos: "+ cursor + "; length: "+(text.length() - cursor),null);
//            GWT.log("text:'"+text+"', cursor: "+(cursor)+", length:"+(text.length() - cursor),null);
        }
    }




    private class DefaultKeyboardListener extends KeyboardListenerAdapter {

        private int lastRequestNumber = 0;

        public void onKeyDown(Widget sender, char keyCode, int modifiers) {
            switch(keyCode) {
                case KEY_DOWN:
                    selectNext();
                    ((TextBox)sender).cancelKey();
                    break;
                case KEY_UP:
                    selectPrevious();
                    ((TextBox)sender).cancelKey();
                    break;
                case KEY_ESCAPE:
                    ((TextBox)sender).setKey((char)KEY_DELETE);
                    hideResult();
                    break;
                case KEY_TAB:
                    setText(getText().substring(0, getCursorPos()));
                    hideResult();
                    break;
                case KEY_ENTER:
                    setCursorPos(getText().length());
                    hideResult();
                    break;
                case KEY_BACKSPACE:
                    setSelectionRange(getCursorPos()-1, getSelectionLength()+1);
                default:
                break;
            }
        }
        public void onKeyUp(Widget sender, char keyCode, int modifiers) {
            switch(keyCode) {
                case KEY_DOWN:  // fall-through
                case KEY_UP:
                    ((TextBox)sender).cancelKey();
                    break;
                case KEY_ESCAPE: // fall-through
                case KEY_ENTER:
                    // record what the user typed (so we can diff from auto-complete on blur)
                    userEntered = getText().substring(0, getCursorPos());
                    break;
                case KEY_TAB:
                    // Tabbing into, set cursor to end
                    // but fall-through for default action as well
                    setCursorPos(getText().length());
                    // fall-through
                default:
                    // record what the user typed (so we can diff from auto-complete on blur)
                    userEntered = getText().substring(0, getCursorPos());
                    // call the service to get the matches
                    matcher.match(
                            userEntered,
                            ++lastRequestNumber,
                            new AsyncCallback() {
                                public void onFailure(Throwable caught) {
                                    Window.alert(caught.toString());
                                }

                                public void onSuccess(Object result) {
                                    MatcherResult match = (MatcherResult) result;
                                    // if this response is for the latest request...
                                    if(lastRequestNumber == match.getRequestNumber()) {
                                        showResult(match);
                                    } else {
                                        GWT.log("Response "+
                                                match.getRequestNumber()+
                                                " was not the latest ("+
                                                lastRequestNumber+").",null);
                                    }
                                }
                            }
                    );
                break;
            }
        }
    }

    /**
     * HACK: This is a temporary override to fix bug in GWT 1.2.11, see http://code.google.com/p/google-web-toolkit/issues/detail?id=334
     *
     * Sets the range of text to be selected.
     *
     * @param pos the position of the first character to be selected
     * @param length the number of characters to be selected
     */
    public void setSelectionRange(int pos, int length) {
//      if (length < 0) {
//        throw new IndexOutOfBoundsException(
//          "Length must be a positive integer. Length: " + length);
//      }
//      if ((pos < 0) || (length + pos > getText().length())) {
//        throw new IndexOutOfBoundsException("From Index: " + pos + "  To Index: "
//          + (pos + length) + "  Text Length: " + getText().length());
//      }
      getImpl().setSelectionRange(getElement(), pos, length);
    }


}
