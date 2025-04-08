package com.ray3k.skincomposer.dialog.scenecomposer.undoables;

public class ImageTextButtonDeleteUndoable extends ActorDeleteUndoable {
    @Override
    public String getRedoString() {
        return "Redo \"Delete ImageTextButton\"";
    }
    
    @Override
    public String getUndoString() {
        return "Undo \"Delete ImageTextButton\"";
    }
}
