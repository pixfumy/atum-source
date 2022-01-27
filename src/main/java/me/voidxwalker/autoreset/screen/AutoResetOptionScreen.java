package me.voidxwalker.autoreset.screen;

import me.voidxwalker.autoreset.Atum;
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
    private Text difficultyText;
    private int difficulty;
    private  ButtonWidget difficultyButton;

    public AutoResetOptionScreen(@Nullable Screen parent) {
        super(Atum.getTranslation("menu.autoresetTitle","Autoreset Options"));
        this.parent = parent;
    }

    protected void init() {
        this.client.keyboard.enableRepeatEvents(true);
        setDifficulty();
        this.seedField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, this.height - 160, 200, 20, Atum.getTranslation("menu.enterSeed","Enter a Seed")) {};
        this.seedField.setText(Atum.seed==null?"":Atum.seed);
        this.seed=Atum.seed;
        this.difficulty=Atum.difficulty;
        this.seedField.setChangedListener((string) -> this.seed = string);
        difficultyButton=this.addButton(new ButtonWidget(this.width / 2 - 75, this.height-100, 150, 20,difficultyText, (buttonWidget) -> {
           this.difficulty= this.difficulty>=4?0: this.difficulty+1;
            setDifficulty();
            buttonWidget.setMessage(difficultyText);
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 155, this.height - 28, 150, 20, Atum.getTranslation("menu.done","Done") , (buttonWidget) -> {
            Atum.seed=seed;
            Atum.difficulty=this.difficulty;
            Atum.saveDifficulty();
            Atum.saveSeed();
            this.client.openScreen(this.parent);
        }));
        this.addButton(new ButtonWidget(this.width / 2 + 5, this.height - 28, 150, 20, ScreenTexts.CANCEL, (buttonWidget) -> this.client.openScreen(this.parent)));
        this.children.add(this.seedField);
        this.setInitialFocus(this.seedField);
    }

    public void removed() {
        this.client.keyboard.enableRepeatEvents(false);
    }

    public void onClose() {
        this.client.openScreen(this.parent);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, this.height - 210, -1);
        drawStringWithShadow(matrices, this.textRenderer, Atum.getTranslation("menu.enterSeed","Seed (Leave empty for a random Seed)").asString(), this.width / 2 - 100, this.height - 180, -6250336);

        this.seedField.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private void setDifficulty() {
        switch (this.difficulty) {
            case 0 :
                difficultyText= Atum.getTranslation("menu.autoreset.peaceful", "Peaceful");
                break;
            case 1 :
                difficultyText = Atum.getTranslation("menu.autoreset.easy", "Easy");
                break;
            case 2 :
                difficultyText = Atum.getTranslation("menu.autoreset.normal", "Normal");
                break;
            case 3 :
                difficultyText = Atum.getTranslation("menu.autoreset.hard", "Hard");
                break;
            case 4 :
                difficultyText = Atum.getTranslation("menu.autoreset.hardcore", "Hardcore");
                break;
            default:
                difficultyText = Atum.getTranslation("menu.autoreset.easy", "Easy");
                break;
        }
    }

}
