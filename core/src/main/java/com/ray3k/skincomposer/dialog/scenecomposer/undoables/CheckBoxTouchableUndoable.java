package com.ray3k.skincomposer.dialog.scenecomposer.undoables;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.ray3k.skincomposer.dialog.scenecomposer.DialogSceneComposer;
import com.ray3k.skincomposer.dialog.scenecomposer.DialogSceneComposerModel.SimCheckBox;

public class CheckBoxTouchableUndoable implements SceneComposerUndoable {
    private SimCheckBox checkBox;
    private DialogSceneComposer dialog;
    private Touchable touchable;
    private Touchable previousTouchable;
    
    public CheckBoxTouchableUndoable(Touchable touchable) {
        this.touchable = touchable;
        dialog = DialogSceneComposer.dialog;
        checkBox = (SimCheckBox) dialog.simActor;
        previousTouchable = checkBox.touchable;
    }
    
    @Override
    public void undo() {
        checkBox.touchable = previousTouchable;
        
        if (dialog.simActor != checkBox) {
            dialog.simActor = checkBox;
            dialog.populateProperties();
            dialog.populatePath();
        }
        dialog.model.updatePreview();
    }
    
    @Override
    public void redo() {
        checkBox.touchable = touchable;
        
        if (dialog.simActor != checkBox) {
            dialog.simActor = checkBox;
            dialog.populateProperties();
            dialog.populatePath();
        }
        dialog.model.updatePreview();
    }
    
    @Override
    public String getRedoString() {
        return "Redo \"CheckBox touchable " + touchable + "\"";
    }
    
    @Override
    public String getUndoString() {
        return "Undo \"CheckBox touchable " + touchable + "\"";
    }
}
