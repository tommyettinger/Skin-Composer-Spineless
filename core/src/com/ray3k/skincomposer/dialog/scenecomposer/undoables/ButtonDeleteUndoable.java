package com.ray3k.skincomposer.dialog.scenecomposer.undoables;

public class ButtonDeleteUndoable extends ActorDeleteUndoable {
    @Override
    public String getRedoString() {
        return "Redo \"Delete Button\"";
    }
    
    @Override
    public String getUndoString() {
        return "Undo \"Delete Button\"";
    }
}
