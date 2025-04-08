package com.ray3k.skincomposer.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;
import com.ray3k.stripe.PopTable;

import static com.ray3k.skincomposer.Main.*;

public class PopRevertUIscale extends PopTable {

    public PopRevertUIscale() {
        var image = new Image(skin.getAtlas().findRegion("tt-icon-emoji"));
        add(image);
        setModal(true);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                hide();
                fire(new PopRevertEvent(false));
            }
        }, 5f);
    }
    
    @Override
    public void show(Stage stage, Action action) {
        super.show(stage, action);
    }
    
    @Override
    public void act(float delta) {
        super.act(delta);
        if (Gdx.input.isButtonJustPressed(Buttons.LEFT)) {
            hide();
            fire(new PopRevertEvent(true));
        }
    }
    
    public static class PopRevertEvent extends Event {
        public boolean accepted;
    
        public PopRevertEvent(boolean accepted) {
            this.accepted = accepted;
        }
    }
    
    public abstract static class PopRevertEventListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            if (event instanceof PopRevertEvent) {
                if (((PopRevertEvent) event).accepted) accepted();
                else reverted();
                return true;
            }
            return false;
        }
    
        public abstract void accepted();
        public abstract void reverted();
    }
}
