package com.ray3k.skincomposer.dialog.scenecomposer.undoables;

public class ImageButtonDeleteUndoable extends ActorDeleteUndoable {
    @Override
    public String getRedoString() {
        return "Redo \"Delete ImageButton\"";
    }
    
    @Override
    public String getUndoString() {
        return "Undo \"Delete ImageButton\"";
    }
}
