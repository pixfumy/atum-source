package me.voidxwalker.autoreset.screen;

import me.voidxwalker.autoreset.Atum;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;


public class AutoResetOptionScreen extends Screen{
    private final Screen parent;
    private TextFieldWidget seedField;
    private String seed;
    private boolean isHardcore;
    private Text difficulty;
    private  ButtonWidget difficultyButton;

    public AutoResetOptionScreen(@Nullable Screen parent) {
        super(Atum.getTranslation("menu.autoresetTitle","Autoreset Options"));
        this.parent = parent;
    }

    protected void init() {
        this.client.keyboard.enableRepeatEvents(true);
        this.isHardcore=Atum.isHardcore;
        setDifficulty();
        this.seedField = new TextFieldWidget(client.textRenderer, this.width / 2 - 100, this.height - 160, 200, 20, Atum.getTranslation("menu.enterSeed","Enter a Seed").asString()) {};
        this.seedField.setText(Atum.seed==null?"":Atum.seed);
        this.seed=Atum.seed;
        this.seedField.setChangedListener((string) -> this.seed = string);
        difficultyButton=this.addButton(new ButtonWidget(this.width / 2 - 75, this.height-100, 150, 20,difficulty.asString() , (buttonWidget) -> {

            this.isHardcore=!this.isHardcore;
            setDifficulty();
            difficultyButton.setMessage(difficulty.asString());
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 155, this.height - 28, 150, 20, I18n.translate("gui.done") , (buttonWidget) -> {
            Atum.seed=seed;
            Atum.isHardcore=this.isHardcore;
            Atum.saveDifficulty();
            Atum.saveSeed();
            this.client.openScreen(this.parent);
        }));
        this.addButton(new ButtonWidget(this.width / 2 + 5, this.height - 28, 150, 20, I18n.translate("gui.cancel"), (buttonWidget) -> this.client.openScreen(this.parent)));
        this.children.add(this.seedField);
        this.setInitialFocus(this.seedField);
    }

    public void removed() {
        this.client.keyboard.enableRepeatEvents(false);
    }

    public void onClose() {
        this.client.openScreen(this.parent);
    }

    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        drawCenteredString(client.textRenderer, this.title.asString(), this.width / 2, this.height - 210, -1);
        drawString( client.textRenderer, Atum.getTranslation("menu.enterSeed","Seed (Leave empty for a random Seed)").asString(), this.width / 2 - 100, this.height - 180, -6250336);

        this.seedField.render( mouseX, mouseY, delta);
        super.render(mouseX, mouseY, delta);
    }

    private void setDifficulty() {
        if(this.isHardcore) {
            difficulty = Atum.getTranslation("menu.autoreset.hardcore-on","Hardcore: ON");
        }
        else {
            difficulty = Atum.getTranslation("menu.autoreset.hardcore-off","Hardcore: OFF");
        }

    }

}
