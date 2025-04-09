package com.ray3k.skincomposer.dialog.scenecomposer.undoables;

public class TextButtonDeleteUndoable extends ActorDeleteUndoable {
    @Override
    public String getRedoString() {
        return "Redo \"Delete TextButton\"";
    }
    
    @Override
    public String getUndoString() {
        return "Undo \"Delete TextButton\"";
    }
}
