package com.ray3k.skincomposer.dialog.textratypist;

import java.util.Locale;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.github.tommyettinger.textra.TypingLabel;
import com.ray3k.stripe.PopColorPicker;
import com.ray3k.stripe.PopColorPicker.PopColorPickerListener;
import com.ray3k.stripe.PopTable;

import static com.ray3k.skincomposer.Main.*;
import static com.ray3k.skincomposer.utils.Utils.isNumeric;
import static com.ray3k.skincomposer.utils.Utils.onChange;

public class PopTextraEffects extends PopTable {
    private String tagBegin, tagEnd;
    private Table tokenTable;
    private SelectBox<String> effectSelectBox;
    private TypingLabel typingLabel;
    private final static String TEST_STRING = "The quick brown fox jumped over the lazy dog.";
    
    public PopTextraEffects() {
        var style = new PopTableStyle();
        style.background = skin.getDrawable("tt-bg");
        style.stageBackground = skin.getDrawable("tt-stage-background");
    
        setStyle(style);
        setModal(true);
        setKeepSizedWithinStage(true);
        setKeepCenteredInWindow(true);
        setAutomaticallyResized(false);
        pad(10);
        
        defaults().space(5);
        var table = new Table();
        add(table);
        
        table.defaults().space(5);
        var label = new Label("Effect: ", skin, "tt");
        table.add(label);
        
        effectSelectBox = new SelectBox<>(skin, "tt");
        var items = new Array<>(new String[]{"Reset",

                "Attention", "Blink", "Cannon", "Carousel", "Crowd", "Ease", "Emerge", "Fade", "Gradient", "Hang",
                "Heartbeat", "Hide", "Highlight", "Instant", "Jolt", "Jump", "Link", "Lockstep", "Meet", "Metronome",
                "Ocean", "Pinch", "Rainbow", "Rotate", "Scale", "Shake", "Shoot", "Shrink", "Sick", "Slam", "Slide",
                "Slip", "Spin", "Spiral", "Sputter", "Squash", "Stylist", "Thinking", "Throb", "Trigger", "Wave",
                "Wind", "Zipper",

                "Black Outline", "White Outline", "Red Outline", "Blue Outline", "Yellow Outline", "Shiny",
                "Drop Shadow", "Neon", "Halo", "Error", "Warn", "Note", "Context", "Suggest", "Jostle", "Small Caps",

                "Wait", "Speed", "Slower", "Slow", "Normal", "Fast", "Faster", "Var", "Event"});
        effectSelectBox.setItems(items);
        effectSelectBox.getList().addListener(handListener);
        table.add(effectSelectBox);
        effectSelectBox.addListener(handListener);
        onChange(effectSelectBox, this::updateTokenTable);
        
        row();
        table = new Table();
        table.setBackground(skin.getDrawable("black"));
        table.left().pad(5);
        add(table).growX().height(50);
        
        typingLabel = new TypingLabel(TEST_STRING, skin, "tt-effect");
        table.add(typingLabel);
        
        row();
        tokenTable = new Table();
        add(tokenTable).grow();
        updateTokenTable();
        
        row();
        table = new Table();
        table.right();
        add(table);
        
        table.defaults().space(5).uniformX().fillX();
        var textButton = new TextButton("OK", skin, "tt");
        table.add(textButton);
        textButton.addListener(handListener);
        onChange(textButton, () -> {
            hide();
            fire(new PopEffectsEvent(tagBegin, tagEnd));
        });
        
        textButton = new TextButton("Cancel", skin, "tt");
        table.add(textButton);
        textButton.addListener(handListener);
        onChange(textButton, () -> {
            hide();
            fire(new PopEffectsEvent());
        });
    }
    
    private void updateTokenTable() {
        tokenTable.clearChildren();
        
        tokenTable.defaults().space(5);

        Runnable runnable;
        TextField distanceField, intensityField, frequencyField, speedField, durationField,
                spreadField, rotationsField, likelihoodField, thresholdField, tiltField,
                sizeXField, sizeYField, distanceXField, distanceYField, spacingField,
                hueField, saturationField, lightnessField, rotateField;
        TextButton elasticButton, allButton, insideButton, underlineButton, strikethroughButton;
        Color color1, color2;
        PopColorPicker color1pop, color2pop;

        switch (effectSelectBox.getSelected()) {
                // TypingLabel Effects

            case "Attention":
                tagBegin = "{ATTENTION}";
                tagEnd = "{ENDATTENTION}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                tokenTable.row();
                spreadField = createNumberField(5.0f, "spread", "sizeY", "sizeY", tokenTable);

                tokenTable.row();
                sizeYField = createNumberField(2.0f, "sizeY", "spread", "spread", tokenTable);

                runnable = () -> {
                    float spread = isNumeric(spreadField.getText()) ? Float.parseFloat(spreadField.getText()) : 5.0f;
                    float sizeY = isNumeric(sizeYField.getText()) ? Float.parseFloat(sizeYField.getText()) : 2.0f;
                    tagBegin = "{ATTENTION=" + spread + ";" + sizeY + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(spreadField, runnable);
                onChange(sizeYField, runnable);
                break;
            case "Blink":
                tagBegin = "{BLINK}";
                tagEnd = "{ENDBLINK}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
    
                color1 = new Color(Color.WHITE);
                color1pop = createColorField(color1, "color1", tokenTable);
    
                tokenTable.row();
                color2 = new Color(Color.BLACK);
                color2pop = createColorField(color2, "color2", tokenTable);
    
                tokenTable.row();
                frequencyField = createNumberField(1.0f, "frequency", "threshold", "threshold", tokenTable);
    
                tokenTable.row();
                thresholdField = createNumberField(.5f, "threshold", "frequency", "frequency", tokenTable);
    
                runnable = () -> {
                    float frequency = isNumeric(frequencyField.getText()) ? Float.parseFloat(frequencyField.getText()) : 1.0f;
                    float threshold = isNumeric(thresholdField.getText()) ? Float.parseFloat(thresholdField.getText()) : .5f;
                    tagBegin = "{BLINK=" + color1 + ";" + color2 + ";" + frequency + ";" + threshold + "}";
        
                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };
    
                color1pop.addListener(new PopColorPickerListener() {
                    @Override
                    public void picked(Color color) {
                        color1.set(color);
                        runnable.run();
                    }

                    @Override
                    public void updated(Color color) {
                    }

                    @Override
                    public void cancelled(Color oldColor) {
                    }
                });
                color2pop.addListener(new PopColorPickerListener() {
                    @Override
                    public void picked(Color color) {
                        color2.set(color);
                        runnable.run();
                    }

                    @Override
                    public void updated(Color color) {
                    }

                    @Override
                    public void cancelled(Color oldColor) {
                    }
                });
                onChange(frequencyField, runnable);
                onChange(thresholdField, runnable);
                break;
            case "Cannon":
                tagBegin = "{CANNON}";
                tagEnd = "{ENDCANNON}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                //initialStretch;extent;height;shakeDuration;shakePower
                spreadField = createNumberField(1.0f, "initial stretch", "shake power", "extent", tokenTable);

                tokenTable.row();
                distanceField = createNumberField(1.0f, "extent", "initial stretch", "height", tokenTable);

                tokenTable.row();
                sizeYField = createNumberField(1.0f, "height", "extent", "shake duration", tokenTable);

                tokenTable.row();
                durationField = createNumberField(2.0f, "shake duration", "height", "shake power", tokenTable);

                tokenTable.row();
                intensityField = createNumberField(1.0f, "shake power", "shake duration", "initial stretch", tokenTable);

                runnable = () -> {
                    float initialStretch = isNumeric(spreadField.getText()) ? Float.parseFloat(spreadField.getText()) : 1.0f;
                    float extent = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 1.0f;
                    float height = isNumeric(sizeYField.getText()) ? Float.parseFloat(sizeYField.getText()) : 1.0f;
                    float duration = isNumeric(durationField.getText()) ? Float.parseFloat(durationField.getText()) : 2.0f;
                    float power = isNumeric(intensityField.getText()) ? Float.parseFloat(intensityField.getText()) : 1.0f;
                    tagBegin = "{CANNON=" + initialStretch + ";" + extent + ";" + height + ";" + duration + ";" + power + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(spreadField, runnable);
                onChange(distanceField, runnable);
                onChange(sizeYField, runnable);
                onChange(durationField, runnable);
                onChange(intensityField, runnable);
                break;
            case "Carousel":
                tagBegin = "{CAROUSEL}";
                tagEnd = "{ENDCAROUSEL}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                tokenTable.row();
                frequencyField = createNumberField(1.0f, "frequency", "duration", "duration", tokenTable);

                tokenTable.row();
                durationField = createNumberField(1.0f, "duration", "frequency", "frequency", tokenTable);

                runnable = () -> {
                    float frequency = isNumeric(frequencyField.getText()) ? Float.parseFloat(frequencyField.getText()) : 1.0f;
                    float duration = isNumeric(durationField.getText()) ? Float.parseFloat(durationField.getText()) : -1.0f;
                    tagBegin = "{CAROUSEL=" + frequency + ";" + (duration >= 0f ? duration : "_") + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(frequencyField, runnable);
                onChange(durationField, runnable);
                break;
            case "Crowd":
                tagBegin = "{CROWD}";
                tagEnd = "{ENDCROWD}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
        
                tokenTable.row();
                distanceField = createNumberField(15.0f, "rotation amount", "duration", "speed", tokenTable);
        
                tokenTable.row();
                speedField = createNumberField(1.0f, "speed", "rotation amount", "duration", tokenTable);
        
                tokenTable.row();
                durationField = createNumberField(-1.0f, "duration", "speed", "rotation amount", tokenTable);
        
                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 15f;
                    float speed = isNumeric(speedField.getText()) ? Float.parseFloat(speedField.getText()) : 1.0f;
                    float duration = isNumeric(durationField.getText()) ? Float.parseFloat(durationField.getText()) : -1.0f;
                    tagBegin = "{CROWD=" + distance + ";" + speed + ";" + (duration >= 0f ? duration : "_") + "}";
            
                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };
        
                onChange(distanceField, runnable);
                onChange(speedField, runnable);
                onChange(durationField, runnable);
                break;
            case "Ease":
                tagBegin = "{EASE}";
                tagEnd = "{ENDEASE}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                distanceField = createNumberField(-2.0f, "distance", "extent", "extent", tokenTable);

                tokenTable.row();
                intensityField = createNumberField(1.0f, "extent", "distance", "distance", tokenTable);

                tokenTable.row();
                elasticButton = createBooleanField(false, "elastic", tokenTable);

                runnable = () -> {
                    float distance = isNumeric(distanceField.getText())? Float.parseFloat(distanceField.getText()) : -2.0f;
                    float extent = isNumeric(intensityField.getText()) ? Float.parseFloat(intensityField.getText()) : 1.0f;
                    tagBegin = "{EASE=" + distance + ";" + extent + ";" + elasticButton.isChecked() + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(distanceField, runnable);
                onChange(intensityField, runnable);
                onChange(elasticButton, runnable);
                break;
            case "Emerge":
                tagBegin = "{EMERGE}";
                tagEnd = "{ENDEMERGE}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                tokenTable.row();
                speedField = createNumberField(1.0f, "speed", "speed", "speed", tokenTable);

                tokenTable.row();
                elasticButton = createBooleanField(false, "elastic", tokenTable);

                runnable = () -> {
                    float intensity = isNumeric(speedField.getText()) ? Float.parseFloat(speedField.getText()) : 1.0f;
                    tagBegin = "{EMERGE=" + intensity + ";" + elasticButton.isChecked() + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(speedField, runnable);
                onChange(elasticButton, runnable);
                break;
            case "Fade":
                tagBegin = "{FADE}";
                tagEnd = "{ENDFADE}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                color1 = new Color(Color.WHITE);
                color1pop = createColorField(color1, "colorOrAlpha1", tokenTable);

                tokenTable.row();
                color2 = new Color(Color.WHITE);
                color2pop = createColorField(color2, "colorOrAlpha2", tokenTable);

                tokenTable.row();
                durationField = createNumberField(1f, "duration", "duration", "duration", tokenTable);

                runnable = () -> {
                    float duration = isNumeric(durationField.getText()) ? Float.parseFloat(durationField.getText()) : 1f;
                    tagBegin = "{FADE=" + color1 + ";" + color2 + ";" + duration + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                color1pop.addListener(new PopColorPickerListener() {
                    @Override
                    public void picked(Color color) {
                        color1.set(color);
                        runnable.run();
                    }

                    @Override
                    public void updated(Color color) {
                    }

                    @Override
                    public void cancelled(Color oldColor) {
                    }
                });
                color2pop.addListener(new PopColorPickerListener() {
                    @Override
                    public void picked(Color color) {
                        color2.set(color);
                        runnable.run();
                    }

                    @Override
                    public void updated(Color color) {
                    }

                    @Override
                    public void cancelled(Color oldColor) {
                    }
                });
                onChange(durationField, runnable);
                break;
            case "Gradient":
                tagBegin = "{GRADIENT}";
                tagEnd = "{ENDGRADIENT}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                color1 = new Color(Color.WHITE);
                color1pop = createColorField(color1, "color1", tokenTable);

                tokenTable.row();
                color2 = Color.valueOf("888888FF");
                color2pop = createColorField(color2, "color2", tokenTable);

                tokenTable.row();
                distanceField = createNumberField(1.0f, "distance", "frequency", "frequency", tokenTable);

                tokenTable.row();
                frequencyField = createNumberField(1.0f, "frequency", "distance", "distance", tokenTable);

                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 1.0f;
                    float frequency = isNumeric(frequencyField.getText()) ? Float.parseFloat(frequencyField.getText()) : 1.0f;
                    tagBegin = "{GRADIENT=" + color1 + ";" + color2 + ";" + distance + ";" + frequency + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                color1pop.addListener(new PopColorPickerListener() {
                    @Override
                    public void picked(Color color) {
                        color1.set(color);
                        runnable.run();
                    }

                    @Override
                    public void updated(Color color) {
                    }

                    @Override
                    public void cancelled(Color oldColor) {
                    }
                });
                color2pop.addListener(new PopColorPickerListener() {
                    @Override
                    public void picked(Color color) {
                        color2.set(color);
                        runnable.run();
                    }

                    @Override
                    public void updated(Color color) {
                    }

                    @Override
                    public void cancelled(Color oldColor) {
                    }
                });
                onChange(distanceField, runnable);
                onChange(frequencyField, runnable);
                break;
            case "Hang":
                tagBegin = "{HANG}";
                tagEnd = "{ENDHANG}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                distanceField = createNumberField(1.0f, "distance", "intensity", "intensity", tokenTable);

                tokenTable.row();
                intensityField = createNumberField(1.0f, "intensity", "distance", "distance", tokenTable);

                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 1.0f;
                    float intensity = isNumeric(intensityField.getText()) ? Float.parseFloat(intensityField.getText()) : 1.0f;
                    tagBegin = "{HANG=" + distance + ";" + intensity + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(distanceField, runnable);
                onChange(intensityField, runnable);
                break;
            case "Heartbeat":
                tagBegin = "{HEARTBEAT}";
                tagEnd = "{ENDHEARTBEAT}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                tokenTable.row();
                distanceField = createNumberField(1.0f, "expansion", "duration", "frequency", tokenTable);

                tokenTable.row();
                frequencyField = createNumberField(1.0f, "frequency", "expansion", "duration", tokenTable);

                tokenTable.row();
                durationField = createNumberField(-1.0f, "duration", "frequency", "expansion", tokenTable);

                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 1.0f;
                    float frequency = isNumeric(frequencyField.getText()) ? Float.parseFloat(frequencyField.getText()) : 1.0f;
                    float duration = isNumeric(durationField.getText()) ? Float.parseFloat(durationField.getText()) : -1.0f;
                    tagBegin = "{HEARTBEAT=" + distance + ";" + frequency + ";" + (duration >= 0f ? duration : "_") + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(distanceField, runnable);
                onChange(frequencyField, runnable);
                onChange(durationField, runnable);
                break;
            case "Hide":
                tagBegin = "{HIDE}";
                tagEnd = "{ENDHIDE}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                color1 = new Color(Color.CLEAR);
                color1pop = createColorField(color1, "color1", tokenTable);

                tokenTable.row();
                color2 = new Color(Color.WHITE);
                color2pop = createColorField(color2, "color2", tokenTable);

                tokenTable.row();
                durationField = createNumberField(1.0f, "duration", "duration", "duration", tokenTable);

                runnable = () -> {
                    float duration = isNumeric(durationField.getText()) ? Float.parseFloat(durationField.getText()) : 1.0f;
                    tagBegin = "{HIDE=" + color1 + ";" + color2 + ";" + duration + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                color1pop.addListener(new PopColorPickerListener() {
                    @Override
                    public void picked(Color color) {
                        color1.set(color);
                        runnable.run();
                    }

                    @Override
                    public void updated(Color color) {
                    }

                    @Override
                    public void cancelled(Color oldColor) {
                    }
                });
                color2pop.addListener(new PopColorPickerListener() {
                    @Override
                    public void picked(Color color) {
                        color2.set(color);
                        runnable.run();
                    }

                    @Override
                    public void updated(Color color) {
                    }

                    @Override
                    public void cancelled(Color oldColor) {
                    }
                });
                onChange(durationField, runnable);
                break;
            case "Highlight":
                tagBegin = "{HIGHLIGHT}";
                tagEnd = "{ENDHIGHLIGHT}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
    
                tokenTable.row();
                color1 = new Color(Color.WHITE);
                color1pop = createColorField(color1, "baseColor", tokenTable);
                
                tokenTable.row();
                distanceField = createNumberField(1.0f, "distance", "frequency", "lightness", tokenTable);
    
                tokenTable.row();
                frequencyField = createNumberField(1.0f, "frequency", "distance", "saturation", tokenTable);
    
                tokenTable.row();
                saturationField = createNumberField(1.0f, "saturation", "frequency", "lightness", tokenTable);
    
                tokenTable.row();
                lightnessField = createNumberField(.5f, "lightness", "saturation", "distance", tokenTable);
    
                tokenTable.row();
                allButton = createBooleanField(false, "all", tokenTable);
    
                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 1.0f;
                    float frequency = isNumeric(frequencyField.getText()) ? Float.parseFloat(frequencyField.getText()) : 1.0f;
                    float saturation = isNumeric(saturationField.getText()) ? Float.parseFloat(saturationField.getText()) : 1.0f;
                    float lightness = isNumeric(lightnessField.getText()) ? Float.parseFloat(lightnessField.getText()) : .5f;
                    tagBegin = "{HIGHLIGHT=" + color1 + ";" + distance + ";" + frequency + ";" + saturation + ";" + lightness + ";" +  allButton.isChecked() + "}";
        
                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };
    
                onChange(distanceField, runnable);
                onChange(frequencyField, runnable);
                onChange(saturationField, runnable);
                onChange(lightnessField, runnable);
                onChange(allButton, runnable);
    
                color1pop.addListener(new PopColorPickerListener() {
                    @Override
                    public void picked(Color color) {
                        color1.set(color);
                        runnable.run();
                    }

                    @Override
                    public void updated(Color color) {
                    }

                    @Override
                    public void cancelled(Color oldColor) {
                    }
                });
                break;
            case "Instant":
                tagBegin = "{INSTANT}";
                tagEnd = "{ENDINSTANT}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
                break;
            case "Jolt":
                tagBegin = "{JOLT}";
                tagEnd = "{ENDJOLT}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                tokenTable.row();
                distanceField = createNumberField(1.0f, "shake distance", "likelihood", "shake speed", tokenTable);

                tokenTable.row();
                intensityField = createNumberField(1.0f, "shake speed", "shake distance", "duration", tokenTable);

                tokenTable.row();
                durationField = createNumberField(-1.0f, "duration", "shake speed", "likelihood", tokenTable);

                tokenTable.row();
                likelihoodField = createNumberField(0.05f, "likelihood", "duration", "shake distance", tokenTable);

                tokenTable.row();
                color1 = new Color(Color.WHITE);
                color1pop = createColorField(color1, "baseColor", tokenTable);

                tokenTable.row();
                color2 = Color.valueOf("FFFF88FF");
                color2pop = createColorField(color2, "joltColor", tokenTable);

                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 1.0f;
                    float intensity = isNumeric(intensityField.getText()) ? Float.parseFloat(intensityField.getText()) : 1.0f;
                    float duration = isNumeric(durationField.getText()) ? Float.parseFloat(durationField.getText()) : -1.0f;
                    float likelihood = isNumeric(likelihoodField.getText()) ? Float.parseFloat(likelihoodField.getText()) : 0.05f;
                    tagBegin = "{JOLT=" + distance + ";" + intensity + ";" + (duration >= 0f ? duration : "_") + ";" + likelihood + ";" + color1 + ";" + color2 + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(distanceField, runnable);
                onChange(intensityField, runnable);
                onChange(durationField, runnable);
                onChange(likelihoodField, runnable);
                color1pop.addListener(new PopColorPickerListener() {
                    @Override
                    public void picked(Color color) {
                        color1.set(color);
                        runnable.run();
                    }

                    @Override
                    public void updated(Color color) {
                    }

                    @Override
                    public void cancelled(Color oldColor) {
                    }
                });
                color2pop.addListener(new PopColorPickerListener() {
                    @Override
                    public void picked(Color color) {
                        color2.set(color);
                        runnable.run();
                    }

                    @Override
                    public void updated(Color color) {
                    }

                    @Override
                    public void cancelled(Color oldColor) {
                    }
                });
                break;
            case "Jump":
                tagBegin = "{JUMP}";
                tagEnd = "{ENDJUMP}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                distanceField = createNumberField(1.0f, "distance", "duration", "frequency", tokenTable);

                tokenTable.row();
                frequencyField = createNumberField(1.0f, "frequency", "distance", "intensity", tokenTable);

                tokenTable.row();
                intensityField = createNumberField(1.0f, "intensity", "frequency", "duration", tokenTable);

                tokenTable.row();
                durationField = createNumberField(-1.0f, "duration", "intensity", "distance", tokenTable);

                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 1.0f;
                    float frequency = isNumeric(frequencyField.getText()) ? Float.parseFloat(frequencyField.getText()) : 1.0f;
                    float intensity = isNumeric(intensityField.getText()) ? Float.parseFloat(intensityField.getText()) : 1.0f;
                    float duration = isNumeric(durationField.getText()) ? Float.parseFloat(durationField.getText()) : -1f;
                    tagBegin = "{JUMP=" + distance + ";" + frequency + ";" + intensity + ";" + (duration >= 0f ? duration : "_") + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(distanceField, runnable);
                onChange(frequencyField, runnable);
                onChange(intensityField, runnable);
                onChange(durationField, runnable);
                break;
            case "Link":
                tagBegin = "{LINK}";
                tagEnd = "{ENDLINK}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                var linkField = createTextField("", "link", "link", "link", tokenTable);

                runnable = () -> {
                    tagBegin = "{LINK=" + linkField.getText() + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(linkField, runnable);
                break;
            case "Lockstep":
                tagBegin = "{LOCKSTEP}";
                tagEnd = "{ENDLOCKSTEP}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                tokenTable.row();
                distanceField = createNumberField(15.0f, "rotation amount", "duration", "tilt", tokenTable);

                tokenTable.row();
                tiltField = createNumberField(0.0f, "tilt", "rotation amount", "speed", tokenTable);

                tokenTable.row();
                speedField = createNumberField(1.0f, "speed", "tilt", "duration", tokenTable);

                tokenTable.row();
                durationField = createNumberField(-1.0f, "duration", "speed", "rotation amount", tokenTable);

                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 15f;
                    float tilt = isNumeric(tiltField.getText()) ? Float.parseFloat(tiltField.getText()) : 0.0f;
                    float speed = isNumeric(speedField.getText()) ? Float.parseFloat(speedField.getText()) : 1.0f;
                    float duration = isNumeric(durationField.getText()) ? Float.parseFloat(durationField.getText()) : -1.0f;
                    tagBegin = "{LOCKSTEP=" + distance + ";" + tilt + ";" + speed + ";" + (duration >= 0f ? duration : "_") + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(distanceField, runnable);
                onChange(tiltField, runnable);
                onChange(speedField, runnable);
                onChange(durationField, runnable);
                break;
            case "Meet":
                tagBegin = "{MEET}";
                tagEnd = "{ENDMEET}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                tokenTable.row();
                distanceField = createNumberField(2.0f, "distance", "extent", "extent", tokenTable);

                tokenTable.row();
                intensityField = createNumberField(1.0f, "extent", "distance", "distance", tokenTable);

                tokenTable.row();
                elasticButton = createBooleanField(false, "elastic", tokenTable);

                tokenTable.row();
                insideButton = createBooleanField(false, "inside", tokenTable);

                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 2.0f;
                    float extent = isNumeric(intensityField.getText()) ? Float.parseFloat(intensityField.getText()) : 1.0f;
                    tagBegin = "{MEET=" + distance + ";" + extent + ";" + elasticButton.isChecked() + ";" + insideButton.isChecked() + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(distanceField, runnable);
                onChange(intensityField, runnable);
                onChange(elasticButton, runnable);
                onChange(insideButton, runnable);
                break;
            case "Metronome":
                tagBegin = "{METRONOME}";
                tagEnd = "{ENDMETRONOME}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                tokenTable.row();
                distanceField = createNumberField(15.0f, "rotation amount", "duration", "tilt", tokenTable);

                tokenTable.row();
                tiltField = createNumberField(0.0f, "tilt", "rotation amount", "speed", tokenTable);

                tokenTable.row();
                speedField = createNumberField(1.0f, "speed", "tilt", "duration", tokenTable);

                tokenTable.row();
                durationField = createNumberField(-1.0f, "duration", "speed", "rotation amount", tokenTable);

                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 15f;
                    float tilt = isNumeric(tiltField.getText()) ? Float.parseFloat(tiltField.getText()) : 0.0f;
                    float speed = isNumeric(speedField.getText()) ? Float.parseFloat(speedField.getText()) : 1.0f;
                    float duration = isNumeric(durationField.getText()) ? Float.parseFloat(durationField.getText()) : -1.0f;
                    tagBegin = "{METRONOME=" + distance + ";" + tilt + ";" + speed + ";" + (duration >= 0f ? duration : "_") + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(distanceField, runnable);
                onChange(tiltField, runnable);
                onChange(speedField, runnable);
                onChange(durationField, runnable);
                break;
            case "Ocean":
                tagBegin = "{OCEAN}";
                tagEnd = "{ENDOCEAN}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                tokenTable.row();
                distanceField = createNumberField(1.0f, "distance", "frequency", "frequency", tokenTable);

                tokenTable.row();
                frequencyField = createNumberField(0.25f, "frequency", "distance", "distance", tokenTable);

                tokenTable.row();
                hueField = createNumberField(0.5f, "hue", "frequency", "saturation", tokenTable);

                tokenTable.row();
                saturationField = createNumberField(0.8f, "saturation", "hue", "lightness", tokenTable);

                tokenTable.row();
                lightnessField = createNumberField(.25f, "lightness", "saturation", "distance", tokenTable);

                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 1.0f;
                    float frequency = isNumeric(frequencyField.getText()) ? Float.parseFloat(frequencyField.getText()) : 0.25f;
                    float hue = isNumeric(hueField.getText()) ? Float.parseFloat(hueField.getText()) : 0.25f;
                    float saturation = isNumeric(saturationField.getText()) ? Float.parseFloat(saturationField.getText()) : 0.25f;
                    float lightness = isNumeric(lightnessField.getText()) ? Float.parseFloat(lightnessField.getText()) : 0.25f;
                    tagBegin = "{OCEAN=" + distance + ";" + frequency + ";" + hue + ";" + saturation + ";" + lightness + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };
                onChange(distanceField, runnable);
                onChange(frequencyField, runnable);
                onChange(hueField, runnable);
                onChange(saturationField, runnable);
                onChange(lightnessField, runnable);
                break;
            case "Pinch":
                tagBegin = "{PINCH}";
                tagEnd = "{ENDPINCH}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                intensityField = createNumberField(0.5f, "strength", "duration", "likelihood", tokenTable);

                tokenTable.row();
                durationField = createNumberField(-1.0f, "duration", "strength", "likelihood", tokenTable);

                tokenTable.row();
                likelihoodField = createNumberField(0.1f, "likelihood", "duration", "strength", tokenTable);

                tokenTable.row();
                elasticButton = createBooleanField(false, "elastic", tokenTable);

                runnable = () -> {
                    float strength = isNumeric(intensityField.getText())? Float.parseFloat(intensityField.getText()) : 0.5f;
                    float duration = isNumeric(durationField.getText()) ? Float.parseFloat(durationField.getText()) : -1f;
                    float likelihood = isNumeric(likelihoodField.getText()) ? Float.parseFloat(likelihoodField.getText()) : 0.1f;
                    tagBegin = "{PINCH=" + strength + ";" + (duration >= 0f ? duration : "_") + ";" + likelihood + ";" + elasticButton.isChecked() + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(intensityField, runnable);
                onChange(durationField, runnable);
                onChange(likelihoodField, runnable);
                onChange(elasticButton, runnable);
                break;
            case "Rainbow":
                tagBegin = "{RAINBOW}";
                tagEnd = "{ENDRAINBOW}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                tokenTable.row();
                distanceField = createNumberField(1.0f, "distance", "frequency", "lightness", tokenTable);

                tokenTable.row();
                frequencyField = createNumberField(1.0f, "frequency", "distance", "saturation", tokenTable);

                tokenTable.row();
                saturationField = createNumberField(1.0f, "saturation", "frequency", "lightness", tokenTable);

                tokenTable.row();
                lightnessField = createNumberField(.5f, "lightness", "saturation", "distance", tokenTable);

                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 1.0f;
                    float frequency = isNumeric(frequencyField.getText()) ? Float.parseFloat(frequencyField.getText()) : 1.0f;
                    float saturation = isNumeric(saturationField.getText()) ? Float.parseFloat(saturationField.getText()) : 1.0f;
                    float lightness = isNumeric(lightnessField.getText()) ? Float.parseFloat(lightnessField.getText()) : .5f;
                    tagBegin = "{RAINBOW=" + distance + ";" + frequency + ";" + saturation + ";" + lightness + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(distanceField, runnable);
                onChange(frequencyField, runnable);
                onChange(saturationField, runnable);
                onChange(lightnessField, runnable);
                break;
            case "Rotate":
                tagBegin = "{ROTATE}";
                tagEnd = "{ENDROTATE}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                tokenTable.row();
                rotateField = createNumberField(90.0f, "rotate", "rotate", "rotate", tokenTable);

                runnable = () -> {
                    float rotation = isNumeric(rotateField.getText()) ? Float.parseFloat(rotateField.getText()) : 90.0f;
                    tagBegin = "{ROTATE=" + rotation + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(rotateField, runnable);
                break;
            case "Scale":
                tagBegin = "{SCALE}";
                tagEnd = "{ENDSCALE}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                tokenTable.row();
                sizeXField = createNumberField(0.75f, "sizeX", "sizeY", "sizeY", tokenTable);

                tokenTable.row();
                sizeYField = createNumberField(2.0f, "sizeY", "sizeX", "sizeX", tokenTable);

                runnable = () -> {
                    float sizeX = isNumeric(sizeXField.getText()) ? Float.parseFloat(sizeXField.getText()) : 0.75f;
                    float sizeY = isNumeric(sizeYField.getText()) ? Float.parseFloat(sizeYField.getText()) : 2.0f;
                    tagBegin = "{SCALE=" + sizeX + ";" + sizeY + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(sizeXField, runnable);
                onChange(sizeYField, runnable);
                break;
            case "Shake":
                tagBegin = "{SHAKE}";
                tagEnd = "{ENDSHAKE}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                distanceField = createNumberField(1.0f, "distance", "duration", "speed", tokenTable);

                tokenTable.row();
                speedField = createNumberField(1.0f, "speed", "distance", "duration", tokenTable);

                tokenTable.row();
                durationField = createNumberField(-1.0f, "duration", "speed", "distance", tokenTable);

                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 1.0f;
                    float intensity = isNumeric(speedField.getText()) ? Float.parseFloat(speedField.getText()) : 1.0f;
                    float duration = isNumeric(durationField.getText()) ? Float.parseFloat(durationField.getText()) : -1.0f;
                    tagBegin = "{SHAKE=" + distance + ";" + intensity + (!MathUtils.isEqual(duration, -1)? ";" + duration: "") + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(distanceField, runnable);
                onChange(speedField, runnable);
                onChange(durationField, runnable);
                break;
            case "Shoot":
                tagBegin = "{SHOOT}";
                tagEnd = "{ENDSHOOT}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                underlineButton = createBooleanField(true, "underline", tokenTable);

                tokenTable.row();
                strikethroughButton = createBooleanField(false, "strikethrough", tokenTable);

                tokenTable.row();
                distanceField = createNumberField(0.3f, "distance", "frequency", "frequency", tokenTable);

                tokenTable.row();
                frequencyField = createNumberField(1.0f, "frequency", "distance", "distance", tokenTable);

                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 0.3f;
                    float frequency = isNumeric(frequencyField.getText()) ? Float.parseFloat(frequencyField.getText()) : 1.0f;
                    tagBegin = "{SHOOT=" + underlineButton.isChecked()+ ";" + strikethroughButton.isChecked() + ";" + distance + ";" + frequency + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(underlineButton, runnable);
                onChange(strikethroughButton, runnable);
                onChange(distanceField, runnable);
                onChange(frequencyField, runnable);
                break;
            case "Shrink":
                tagBegin = "{SHRINK}";
                tagEnd = "{ENDSHRINK}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                tokenTable.row();
                distanceField = createNumberField(1.0f, "expansion", "extent", "extent", tokenTable);

                tokenTable.row();
                intensityField = createNumberField(1.0f, "extent", "expansion", "expansion", tokenTable);

                tokenTable.row();
                elasticButton = createBooleanField(false, "elastic", tokenTable);

                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 1.0f;
                    float intensity = isNumeric(intensityField.getText()) ? Float.parseFloat(intensityField.getText()) : 1.0f;
                    tagBegin = "{SHRINK=" + distance + ";" + intensity + ";" + elasticButton.isChecked() + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(distanceField, runnable);
                onChange(intensityField, runnable);
                onChange(elasticButton, runnable);
                break;
            case "Sick":
                tagBegin = "{SICK}";
                tagEnd = "{ENDSICK}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                distanceField = createNumberField(1.0f, "distance", "duration", "speed", tokenTable);

                tokenTable.row();
                speedField = createNumberField(1.0f, "speed", "distance", "duration", tokenTable);

                tokenTable.row();
                durationField = createNumberField(-1.0f, "duration", "speed", "distance", tokenTable);

                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 1.0f;
                    float speed = isNumeric(speedField.getText()) ? Float.parseFloat(speedField.getText()) : 1.0f;
                    float duration = isNumeric(durationField.getText()) ? Float.parseFloat(durationField.getText()) : -1.0f;
                    tagBegin = "{SICK=" + distance + ";" + speed + ";" + (duration >= 0f ? duration : "_") + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(distanceField, runnable);
                onChange(speedField, runnable);
                onChange(durationField, runnable);
                break;
            case "Slam":
                tagBegin = "{SLAM}";
                tagEnd = "{ENDSLAM}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                //hangTime;extent;height;shakeDuration;shakePower
                spreadField = createNumberField(0.25f, "hang time", "shake power", "extent", tokenTable);

                tokenTable.row();
                distanceField = createNumberField(1.0f, "extent", "hang time", "height", tokenTable);

                tokenTable.row();
                sizeYField = createNumberField(1.0f, "height", "extent", "shake duration", tokenTable);

                tokenTable.row();
                durationField = createNumberField(2.0f, "shake duration", "height", "shake power", tokenTable);

                tokenTable.row();
                intensityField = createNumberField(1.0f, "shake power", "shake duration", "hang time", tokenTable);

                runnable = () -> {
                    float hangTime = isNumeric(spreadField.getText()) ? Float.parseFloat(spreadField.getText()) : 0.25f;
                    float extent = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 1.0f;
                    float height = isNumeric(sizeYField.getText()) ? Float.parseFloat(sizeYField.getText()) : 1.0f;
                    float duration = isNumeric(durationField.getText()) ? Float.parseFloat(durationField.getText()) : 2.0f;
                    float power = isNumeric(intensityField.getText()) ? Float.parseFloat(intensityField.getText()) : 1.0f;
                    tagBegin = "{SLAM=" + hangTime + ";" + extent + ";" + height + ";" + duration + ";" + power + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(spreadField, runnable);
                onChange(distanceField, runnable);
                onChange(sizeYField, runnable);
                onChange(durationField, runnable);
                onChange(intensityField, runnable);
                break;
            case "Slide":
                tagBegin = "{SLIDE}";
                tagEnd = "{ENDSLIDE}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                distanceField = createNumberField(1.0f, "distance", "intensity", "intensity", tokenTable);

                tokenTable.row();
                intensityField = createNumberField(1.0f, "intensity", "distance", "distance", tokenTable);

                tokenTable.row();
                elasticButton = createBooleanField(false, "elastic", tokenTable);

                runnable = () -> {
                    float distance = isNumeric(distanceField.getText())? Float.parseFloat(distanceField.getText()) : 1.0f;
                    float intensity = isNumeric(intensityField.getText()) ? Float.parseFloat(intensityField.getText()) : 1.0f;
                    tagBegin = "{SLIDE=" + distance + ";" + intensity + ";" + elasticButton.isChecked() + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(distanceField, runnable);
                onChange(intensityField, runnable);
                onChange(elasticButton, runnable);
                break;
            case "Slip":
                tagBegin = "{SLIP}";
                tagEnd = "{ENDSLIP}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                distanceField = createNumberField(5.0f, "distance", "speed", "duration", tokenTable);

                tokenTable.row();
                speedField = createNumberField(1.0f, "speed", "distance", "duration", tokenTable);

                tokenTable.row();
                durationField = createNumberField(-1.0f, "duration", "speed", "distance", tokenTable);

                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 5.0f;
                    float speed = isNumeric(speedField.getText()) ? Float.parseFloat(speedField.getText()) : 1.0f;
                    float duration = isNumeric(durationField.getText()) ? Float.parseFloat(durationField.getText()) : -1.0f;
                    tagBegin = "{SLIP=" + distance + ";" + speed +  ";" + (duration >= 0f ? duration : "_") + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(distanceField, runnable);
                onChange(speedField, runnable);
                onChange(durationField, runnable);
                break;
            case "Spin":
                tagBegin = "{SPIN}";
                tagEnd = "{ENDSPIN}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                tokenTable.row();
                intensityField = createNumberField(1.0f, "intensity", "rotations", "rotations", tokenTable);

                tokenTable.row();
                rotationsField = createNumberField(1.0f, "rotations", "intensity", "intensity", tokenTable);

                tokenTable.row();
                elasticButton = createBooleanField(false, "elastic", tokenTable);

                runnable = () -> {
                    float intensity = isNumeric(intensityField.getText()) ? Float.parseFloat(intensityField.getText()) : 1.0f;
                    float rotations = isNumeric(rotationsField.getText()) ? Float.parseFloat(rotationsField.getText()) : 1.0f;
                    tagBegin = "{SPIN=" + intensity + ";" + rotations + ";" + elasticButton.isChecked() + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(intensityField, runnable);
                onChange(rotationsField, runnable);
                onChange(elasticButton, runnable);
                break;
            case "Spiral":
                tagBegin = "{SPIRAL}";
                tagEnd = "{ENDSPIRAL}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                tokenTable.row();
                distanceField = createNumberField(1.0f, "distance", "rotations", "speed", tokenTable);

                tokenTable.row();
                intensityField = createNumberField(1.0f, "speed", "distance", "rotations", tokenTable);

                tokenTable.row();
                rotationsField = createNumberField(1.0f, "rotations", "speed", "distance", tokenTable);

                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 1.0f;
                    float intensity = isNumeric(intensityField.getText()) ? Float.parseFloat(intensityField.getText()) : 1.0f;
                    float rotations = isNumeric(rotationsField.getText()) ? Float.parseFloat(rotationsField.getText()) : 1.0f;
                    tagBegin = "{SPIRAL=" + distance + ";" + intensity + ";" + rotations + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(distanceField, runnable);
                onChange(intensityField, runnable);
                onChange(rotationsField, runnable);
                break;
            case "Sputter":
                tagBegin = "{SPUTTER}";
                tagEnd = "{ENDSPUTTER}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                sizeXField = createNumberField(0.25f, "widen", "duration", "heighten", tokenTable);

                tokenTable.row();
                sizeYField = createNumberField(0.25f, "heighten", "widen", "speed", tokenTable);

                tokenTable.row();
                speedField = createNumberField(1.0f, "speed", "heighten", "duration", tokenTable);

                tokenTable.row();
                durationField = createNumberField(-1.0f, "duration", "speed", "widen", tokenTable);

                runnable = () -> {
                    float widen = isNumeric(sizeXField.getText()) ? Float.parseFloat(sizeXField.getText()) : 0.25f;
                    float heighten = isNumeric(sizeYField.getText()) ? Float.parseFloat(sizeYField.getText()) : 0.25f;
                    float speed = isNumeric(speedField.getText()) ? Float.parseFloat(speedField.getText()) : 1.0f;
                    float duration = isNumeric(durationField.getText()) ? Float.parseFloat(durationField.getText()) : -1f;
                    tagBegin = "{SPUTTER=" + widen + ";" + heighten + ";" + speed + ";" + (duration >= 0f ? duration : "_") + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(sizeXField, runnable);
                onChange(sizeYField, runnable);
                onChange(speedField, runnable);
                onChange(durationField, runnable);
                break;
            case "Squash":
                tagBegin = "{SQUASH}";
                tagEnd = "{ENDSQUASH}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                tokenTable.row();
                intensityField = createNumberField(1.0f, "intensity", "intensity", "intensity", tokenTable);

                tokenTable.row();
                elasticButton = createBooleanField(false, "elastic", tokenTable);

                runnable = () -> {
                    float intensity = isNumeric(intensityField.getText()) ? Float.parseFloat(intensityField.getText()) : 1.0f;
                    tagBegin = "{SQUASH=" + intensity + ";" + elasticButton.isChecked() + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(intensityField, runnable);
                onChange(elasticButton, runnable);
                break;
            case "Stylist":
                tagBegin = "{STYLIST}";
                tagEnd = "{ENDSTYLIST}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
    
                tokenTable.row();
                var boldButton = createBooleanField(false, "bold", tokenTable);
    
                tokenTable.row();
                var obliqueButton = createBooleanField(false, "oblique", tokenTable);
    
                tokenTable.row();
                underlineButton = createBooleanField(false, "underline", tokenTable);
    
                tokenTable.row();
                strikethroughButton = createBooleanField(false, "strikethrough", tokenTable);
                
                tokenTable.row();
                Table table = new Table();
                table.defaults().space(10);
                tokenTable.add(table);
                
                var buttonGroup = new ButtonGroup<>();
                var noScriptButton = createBooleanField(true, "no script", table);
                buttonGroup.add(noScriptButton);

                var subscriptButton = createBooleanField(true, "subscript", table);
                buttonGroup.add(subscriptButton);

                var midscriptButton = createBooleanField(true, "midscript", table);
                buttonGroup.add(midscriptButton);

                var superscriptButton = createBooleanField(true, "superscript", table);
                buttonGroup.add(superscriptButton);
                
                noScriptButton.setChecked(true);
    
                tokenTable.row();
                allButton = createBooleanField(true, "all", tokenTable);
        
                runnable = () -> {
                    tagBegin = "{Stylist=" + boldButton.isChecked() + ";" + obliqueButton.isChecked() + ";" + underlineButton.isChecked() + ";" + strikethroughButton.isChecked() + ";" +  buttonGroup.getCheckedIndex() + ";" + allButton.isChecked() + "}";
            
                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };
        
                onChange(boldButton, runnable);
                onChange(obliqueButton, runnable);
                onChange(underlineButton, runnable);
                onChange(strikethroughButton, runnable);
                onChange(noScriptButton, runnable);
                onChange(subscriptButton, runnable);
                onChange(midscriptButton, runnable);
                onChange(superscriptButton, runnable);
                onChange(allButton, runnable);
                break;
            case "Thinking":
                tagBegin = "{THINKING}";
                tagEnd = "{ENDTHINKING}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                tokenTable.row();
                distanceField = createNumberField(2.0f, "distance", "drift", "extent", tokenTable);

                tokenTable.row();
                intensityField = createNumberField(1.0f, "extent", "distance", "drift", tokenTable);

                tokenTable.row();
                thresholdField = createNumberField(1.0f, "drift", "extent", "distance", tokenTable);

                tokenTable.row();
                insideButton = createBooleanField(false, "inside", tokenTable);

                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 2.0f;
                    float extent = isNumeric(intensityField.getText()) ? Float.parseFloat(intensityField.getText()) : 1.0f;
                    float drift = isNumeric(thresholdField.getText()) ? Float.parseFloat(thresholdField.getText()) : 1.0f;
                    tagBegin = "{THINKING=" + distance + ";" + extent + ";" + drift + ";" + insideButton.isChecked() + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(distanceField, runnable);
                onChange(intensityField, runnable);
                onChange(thresholdField, runnable);
                onChange(insideButton, runnable);
                break;
            case "Throb":
                tagBegin = "{THROB}";
                tagEnd = "{ENDTHROB}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                distanceField = createNumberField(1.0f, "distance", "duration", "speed", tokenTable);

                tokenTable.row();
                speedField = createNumberField(1.0f, "speed", "distance", "frequency", tokenTable);

                tokenTable.row();
                frequencyField = createNumberField(1.0f, "frequency", "speed", "duration", tokenTable);

                tokenTable.row();
                durationField = createNumberField(-1.0f, "duration", "frequency", "distance", tokenTable);

                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 1.0f;
                    float speed = isNumeric(speedField.getText()) ? Float.parseFloat(speedField.getText()) : 1.0f;
                    float frequency = isNumeric(frequencyField.getText()) ? Float.parseFloat(frequencyField.getText()) : 1.0f;
                    float duration = isNumeric(durationField.getText()) ? Float.parseFloat(durationField.getText()) : -1f;
                    tagBegin = "{THROB=" + distance + ";" + speed + ";" + frequency + ";" + (duration >= 0f ? duration : "_") + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(distanceField, runnable);
                onChange(speedField, runnable);
                onChange(frequencyField, runnable);
                onChange(durationField, runnable);
                break;
            case "Trigger":
                tagBegin = "{TRIGGER}";
                tagEnd = "{ENDTRIGGER}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                var triggerField = createTextField("", "event", "event", "event", tokenTable);

                runnable = () -> {
                    tagBegin = "{TRIGGER=" + triggerField.getText() + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(triggerField, runnable);
                break;
            case "Wave":
                tagBegin = "{WAVE}";
                tagEnd = "{ENDWAVE}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                distanceField = createNumberField(1.0f, "distance", "duration", "frequency", tokenTable);

                tokenTable.row();
                frequencyField = createNumberField(1.0f, "frequency", "distance", "intensity", tokenTable);

                tokenTable.row();
                intensityField = createNumberField(1.0f, "intensity", "frequency", "duration", tokenTable);

                tokenTable.row();
                durationField = createNumberField(-1.0f, "duration", "intensity", "distance", tokenTable);

                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 1.0f;
                    float frequency = isNumeric(frequencyField.getText()) ? Float.parseFloat(frequencyField.getText()) : 1.0f;
                    float intensity = isNumeric(intensityField.getText()) ? Float.parseFloat(intensityField.getText()) : 1.0f;
                    float duration = isNumeric(durationField.getText()) ? Float.parseFloat(durationField.getText()) : -1.0f;
                    tagBegin = "{WAVE=" + distance + ";" + frequency + ";" + intensity + (!MathUtils.isEqual(duration, -1)? ";" + duration: "") + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(distanceField, runnable);
                onChange(frequencyField, runnable);
                onChange(intensityField, runnable);
                onChange(durationField, runnable);
                break;
            case "Wind":
                tagBegin = "{WIND}";
                tagEnd = "{ENDWIND}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                distanceXField = createNumberField(1.0f, "distanceX", "duration", "distanceY", tokenTable);

                tokenTable.row();
                distanceYField = createNumberField(1.0f, "distanceY", "distanceX", "spacing", tokenTable);

                tokenTable.row();
                spacingField = createNumberField(1.0f, "spacing", "distanceY", "intensity", tokenTable);

                tokenTable.row();
                intensityField = createNumberField(1.0f, "intensity", "spacing", "duration", tokenTable);

                tokenTable.row();
                durationField = createNumberField(-1.0f, "duration", "intensity", "distanceX", tokenTable);

                runnable = () -> {
                    float distanceX = isNumeric(distanceXField.getText()) ? Float.parseFloat(distanceXField.getText()) : 1.0f;
                    float distanceY = isNumeric(distanceYField.getText()) ? Float.parseFloat(distanceYField.getText()) : 1.0f;
                    float spacing = isNumeric(spacingField.getText()) ? Float.parseFloat(spacingField.getText()) : 1.0f;
                    float intensity = isNumeric(intensityField.getText()) ? Float.parseFloat(intensityField.getText()) : 1.0f;
                    float duration = isNumeric(durationField.getText()) ? Float.parseFloat(durationField.getText()) : -1.0f;
                    tagBegin = "{WIND=" + distanceX + ";" + distanceY + ";" + spacing + ";" + intensity + (!MathUtils.isEqual(duration, -1)? ";" + duration: "") + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(distanceXField, runnable);
                onChange(distanceYField, runnable);
                onChange(spacingField, runnable);
                onChange(intensityField, runnable);
                onChange(durationField, runnable);
                break;
            case "Zipper":
                tagBegin = "{ZIPPER}";
                tagEnd = "{ENDZIPPER}";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();

                distanceField = createNumberField(2.0f, "distance", "extent", "extent", tokenTable);

                tokenTable.row();
                intensityField = createNumberField(1.0f, "extent", "distance", "distance", tokenTable);

                tokenTable.row();
                elasticButton = createBooleanField(false, "elastic", tokenTable);

                runnable = () -> {
                    float distance = isNumeric(distanceField.getText())? Float.parseFloat(distanceField.getText()) : -8.0f;
                    float intensity = isNumeric(intensityField.getText()) ? Float.parseFloat(intensityField.getText()) : 2.0f;
                    tagBegin = "{ZIPPER=" + distance + ";" + intensity + ";" + elasticButton.isChecked() + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };

                onChange(distanceField, runnable);
                onChange(intensityField, runnable);
                onChange(elasticButton, runnable);
                break;

                // Modes

            case "Black Outline":
                tagBegin = "[#]";
                tagEnd = "[#]";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
                break;
            case "White Outline":
                tagBegin = "[?WHITE OUTLINE]";
                tagEnd = "[?][#]";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
                break;
            case "Red Outline":
                tagBegin = "[?RED OUTLINE]";
                tagEnd = "[?][#]";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
                break;
            case "Blue Outline":
                tagBegin = "[?BLUE OUTLINE]";
                tagEnd = "[?][#]";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
                break;
            case "Yellow Outline":
                tagBegin = "[?YELLOW OUTLINE]";
                tagEnd = "[?][#]";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
                break;
            case "Shiny":
                tagBegin = "[?SHINY]";
                tagEnd = "[?]";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
                break;
            case "Drop Shadow":
                tagBegin = "[?SHADOW]";
                tagEnd = "[?]";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
                break;
            case "Neon":
                tagBegin = "[?NEON]";
                tagEnd = "[?]";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
                break;
            case "Halo":
                tagBegin = "[?HALO]";
                tagEnd = "[?]";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
                break;
            case "Error":
                tagBegin = "[?ERROR]";
                tagEnd = "[?]";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
                break;
            case "Warn":
                tagBegin = "[?WARN]";
                tagEnd = "[?]";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
                break;
            case "Note":
                tagBegin = "[?NOTE]";
                tagEnd = "[?]";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
                break;
            case "Context":
                tagBegin = "[?CONTEXT]";
                tagEnd = "[?]";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
                break;
            case "Suggest":
                tagBegin = "[?SUGGEST]";
                tagEnd = "[?]";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
                break;
            case "Jostle":
                tagBegin = "[?JOSTLE]";
                tagEnd = "[?]";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
                break;
            case "Small Caps":
                tagBegin = "[?SMALLCAPS]";
                tagEnd = "[?]";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
                break;

                // Built-in tags

            case "Reset":
                tagBegin = "{RESET}";
                tagEnd = "";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
                break;
            case "Wait":
                tagBegin = "{WAIT}";
                tagEnd = "";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
    
                var secondsField = createNumberField(.25f, "seconds", "seconds", "seconds", tokenTable);
    
                runnable = () -> {
                    float seconds = isNumeric(secondsField.getText())? Float.parseFloat(secondsField.getText()) : .25f;
                    tagBegin = "{WAIT=" + seconds + "}";
        
                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };
    
                onChange(secondsField, runnable);
                break;
            case "Speed":
                tagBegin = "{SPEED}";
                tagEnd = "";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
    
                speedField = createNumberField(1f, "speed", "speed", "speed", tokenTable);
    
                runnable = () -> {
                    float speed = isNumeric(speedField.getText())? Float.parseFloat(speedField.getText()) : 1f;
                    tagBegin = "{SPEED=" + speed + "}";
                    tagEnd = "{SPEED}";
        
                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };
    
                onChange(speedField, runnable);
                break;
            case "Slower":
                tagBegin = "{SLOWER}";
                tagEnd = "";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
                break;
            case "Slow":
                tagBegin = "{SLOW}";
                tagEnd = "";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
                break;
            case "Normal":
                tagBegin = "{NORMAL}";
                tagEnd = "";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
                break;
            case "Fast":
                tagBegin = "{FAST}";
                tagEnd = "";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
                break;
            case "Faster":
                tagBegin = "{FASTER}";
                tagEnd = "";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
                break;
            case "Var":
                tagBegin = "{VAR=}";
                tagEnd = "";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
    
                var varField = createTextField("", "var", "var", "var", tokenTable);
    
                runnable = () -> {
                    tagBegin = "{VAR=" + varField.getText() + "}";
        
                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    typingLabel.restart();
                };
    
                onChange(varField, runnable);
                break;
            case "Event":
                tagBegin = "{EVENT=}";
                tagEnd = "";
                typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                typingLabel.restart();
    
                var eventField = createTextField("", "event", "event", "event", tokenTable);

                runnable = () -> {
                    var text = eventField.getText();
                    tagBegin = "{EVENT=" + text + "}";

                    typingLabel.setText(tagBegin + TEST_STRING + tagEnd);
                    if (!"".equals(text)) {
                        typingLabel.clearVariables();
                        typingLabel.setVariable(text, "Replacement-Test");
                    }
                    typingLabel.restart();
                };

                onChange(eventField, runnable);
                break;
        }
    }
    
    private TextField createNumberField(float defaultValue, String name, String previousField, String nextField, Table table) {
        var subTable = new Table();
        
        subTable.defaults().space(5);
        var label = new Label(name, skin, "tt");
        subTable.add(label);
        
        var textField = new TextField(String.format(Locale.US, "%.1f", defaultValue), skin, "tt") {
            @Override
            public TextField next(boolean up) {
                Actor actor = findActor(up?previousField:nextField);
                stage.setKeyboardFocus(actor);
                if(actor instanceof TextField) return (TextField) actor;
                return null;
            }
        };
        textField.setName(name);
        subTable.add(textField).width(50);
        textField.addListener(ibeamListener);
        applyFieldListener(textField);
    
        var buttonTable = new Table();
        subTable.add(buttonTable);
    
        buttonTable.defaults().space(3);
        var imageButton = new ImageButton(skin, "tt-increase");
        buttonTable.add(imageButton);
        imageButton.addListener(handListener);
        onChange(imageButton, () -> {
            if (!textField.getText().isEmpty()) textField.setText(
                    String.format(Locale.US, "%.1f", Float.parseFloat(textField.getText()) + .1f));
            textField.fire(new ChangeEvent());
        });
    
        buttonTable.row();
        imageButton = new ImageButton(skin, "tt-decrease");
        buttonTable.add(imageButton);
        imageButton.addListener(handListener);
        onChange(imageButton, () -> {
            if (!textField.getText().isEmpty()) textField.setText(
                    String.format(Locale.US, "%.1f", Float.parseFloat(textField.getText()) - .1f));
            textField.fire(new ChangeEvent());
        });
        
        table.add(subTable);
        return textField;
    }
    
    private TextField createTextField(String defaultValue, String name, String previousField, String nextField, Table table) {
        var subTable = new Table();
        
        subTable.defaults().space(5);
        var label = new Label(name, skin, "tt");
        subTable.add(label);
        
        var textField = new TextField(defaultValue, skin, "tt") {
            @Override
            public TextField next(boolean up) {
                Actor actor = findActor(up?previousField:nextField);
                stage.setKeyboardFocus(actor);
                if(actor instanceof TextField) return (TextField) actor;
                return null;

            }
        };
        textField.setName(name);
        subTable.add(textField).width(200);
        textField.addListener(ibeamListener);
        applyFieldListener(textField);
        
        table.add(subTable);
        return textField;
    }
    
    private TextButton createBooleanField(boolean defaultValue, String name, Table table) {
        var textButton = new TextButton(name, skin, "tt-toggle");
        textButton.setChecked(defaultValue);
        table.add(textButton);
        textButton.addListener(handListener);
        return textButton;
    }
    
    private PopColorPicker createColorField(Color defaultValue, String name, Table table) {
        var pop = new PopColorPicker(defaultValue, PopTextraTypist.ttColorPickerStyle);
        pop.setButtonListener(handListener);
        pop.setTextFieldListener(ibeamListener);
        
        var subTable = new Table();
        table.add(subTable);
        
        subTable.defaults().space(5);
        var label = new Label(name, skin, "tt");
        subTable.add(label);
        
        var imageButton = new ImageButton(skin, "tt-swatch");
        imageButton.setColor(defaultValue);
        subTable.add(imageButton);
        imageButton.addListener(handListener);
        pop.addListener(new PopColorPickerListener() {
            @Override
            public void picked(Color color) {
                imageButton.getImage().setColor(color);
            }

            @Override
            public void updated(Color color) {
            }

            @Override
            public void cancelled(Color oldColor) {
            }
        });
        onChange(imageButton, () -> pop.show(stage));
        return pop;
    }
    
    private void applyFieldListener(TextField textField) {
        textField.removeListener(textField.getDefaultInputListener());
        textField.addListener(new ClickListener() {
            boolean selectAll;
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!selectAll) ((ClickListener)textField.getDefaultInputListener()).clicked(event, x, y);
                else {
                    textField.selectAll();
                }
            }
            
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                selectAll = stage.getKeyboardFocus() != event.getListenerActor();
                textField.getDefaultInputListener().touchDown(event, x, y, pointer, button);
                return super.touchDown(event, x, y, pointer, button);
            }
            
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                textField.getDefaultInputListener().touchDragged(event, x, y, pointer);
                super.touchDragged(event, x, y, pointer);
            }
            
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                textField.getDefaultInputListener().touchUp(event, x, y, pointer, button);
                super.touchUp(event, x, y, pointer, button);
            }
            
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                return textField.getDefaultInputListener().keyDown(event, keycode);
            }
            
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                return textField.getDefaultInputListener().keyUp(event, keycode);
            }
            
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                return textField.getDefaultInputListener().keyTyped(event, character);
            }
        });
    }
    
    public static class PopEffectsEvent extends Event {
        String tagBegin;
        String tagEnd;
    
        public PopEffectsEvent(String tagBegin, String tagEnd) {
            this.tagBegin = tagBegin;
            this.tagEnd = tagEnd;
        }
    
        public PopEffectsEvent() {
        }
    }
    
    public static abstract class PopEffectsListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            if (event instanceof PopEffectsEvent) {
                var popEffectsEvent = (PopEffectsEvent) event;
                if (popEffectsEvent.tagBegin != null) accepted(popEffectsEvent.tagBegin, popEffectsEvent.tagEnd);
                else cancelled();
            }
            return false;
        }
        
        public abstract void accepted(String tagBegin, String tagEnd);
        public abstract void cancelled();
    }
    
    public static PopTextraEffects showPopEffects() {
        var pop = new PopTextraEffects();
        pop.show(stage);
        pop.setSize(400, 350);
        return pop;
    }
}