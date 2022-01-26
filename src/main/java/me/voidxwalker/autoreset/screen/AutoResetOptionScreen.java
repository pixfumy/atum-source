package me.voidxwalker.autoreset.screen;

import me.voidxwalker.autoreset.Main;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;


public class AutoResetOptionScreen extends Screen{
    private final Screen parent;
    private TextFieldWidget seedField;
    private String seed;
    private Text difficulty;
    private  ButtonWidget difficultyButton;

    public AutoResetOptionScreen(@Nullable Screen parent) {
        super(Main.getTranslation("menu.autoresetTitle","Autoreset Options"));
        this.parent = parent;
    }

    protected void init() {
        this.client.keyboard.setRepeatEvents(true);
        setDifficulty();
        this.seedField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, this.height - 160, 200, 20, Main.getTranslation("menu.enterSeed","Enter a Seed")) {};
        this.seedField.setText(seed==null?"":seed);
        this.seedField.setChangedListener((string) -> this.seed = string);
        difficultyButton=this.addDrawableChild(new ButtonWidget(this.width / 2 - 75, this.height-100, 150, 20,difficulty , (buttonWidget) -> {

            Main.difficulty= Main.difficulty==4?0: Main.difficulty+1;
            setDifficulty();
            difficultyButton.setMessage(difficulty);
        }));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 155, this.height - 28, 150, 20,Main.getTranslation("menu.done","Done") , (buttonWidget) -> {
            Main.seed=seed;
            Main.saveDifficulty();
            Main.saveSeed();
            this.client.setScreen(this.parent);
        }));
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 5, this.height - 28, 150, 20, ScreenTexts.CANCEL, (buttonWidget) -> this.client.setScreen(this.parent)));
        this.addSelectableChild(this.seedField);
        this.setInitialFocus(this.seedField);
    }

    public void removed() {
        this.client.keyboard.setRepeatEvents(false);
    }

    public void onClose() {
        this.client.setScreen(this.parent);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, this.height - 210, -1);
        drawStringWithShadow(matrices, this.textRenderer, Main.getTranslation("menu.enterSeed","Seed (Leave empty for a random Seed)").asString(), this.width / 2 - 100, this.height - 180, -6250336);

        this.seedField.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private void setDifficulty() {
        switch (Main.difficulty) {
            case 0 :
                difficulty = Main.getTranslation("menu.autoreset.peaceful", "Peaceful");
                break;
            case 1 :
                difficulty = Main.getTranslation("menu.autoreset.easy", "Easy");
                break;
            case 2 :
                difficulty = Main.getTranslation("menu.autoreset.normal", "Normal");
                break;
            case 3 :
                difficulty = Main.getTranslation("menu.autoreset.hard", "Hard");
                break;
            case 4 :
                difficulty = Main.getTranslation("menu.autoreset.hardcore", "Hardcore");
                break;
            default:
                difficulty = Main.getTranslation("menu.autoreset.easy", "Easy");
                break;
        }
    }

}
