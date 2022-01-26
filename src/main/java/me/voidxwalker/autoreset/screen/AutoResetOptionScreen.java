package me.voidxwalker.autoreset.screen;

import me.voidxwalker.autoreset.Main;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;


public class AutoResetOptionScreen extends Screen{
    private final Screen parent;
    private TextFieldWidget seedField;
    private String seed;
    private String difficulty;
    protected String title = "Autoreset Options";
    public AutoResetOptionScreen(@Nullable Screen parent) {
        super();
        this.parent = parent;
    }

    public void init() {
        setDifficulty();
        seed=Main.seed;
        this.seedField = new TextFieldWidget(6969696,client.textRenderer, this.width / 2 - 100, this.height - 160, 200, 20) ;
        this.seedField.setText(Main.seed==null?"":Main.seed);
        this.seedField.setFocused(true);
        this.buttons.add(new ButtonWidget(1,this.width / 2 - 75, this.height-100, 150, 20,difficulty));
        this.buttons.add(new ButtonWidget(2,this.width / 2 - 155, this.height - 28, 150, 20, I18n.translate("gui.done") ));
        this.buttons.add(new ButtonWidget(3,this.width / 2 + 5, this.height - 28, 150, 20, I18n.translate("gui.cancel")));
    }

    protected void buttonClicked(ButtonWidget button) {
        switch(button.id) {

            case 1:
                Main.isHardcore=!Main.isHardcore;
                setDifficulty();
                button.message=difficulty;
                break;
            case 2:
                Main.seed=seed;
                Main.saveDifficulty();
                Main.saveSeed();
                this.client.openScreen(this.parent);
                break;
            case 3:
                this.client.openScreen(this.parent);
                break;
            default:
                break;

        }

    }

public void tick(){
        seedField.tick();
}

    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        drawCenteredString(client.textRenderer, this.title, this.width / 2, this.height - 210, -1);
        this.drawWithShadow( client.textRenderer, "Seed (Leave empty for a random Seed)", this.width / 2 - 100, this.height - 180, -6250336);

        this.seedField.render();
        super.render(mouseX, mouseY, delta);
    }

    private void setDifficulty() {
        if(Main.isHardcore) {
            difficulty = "Hardcore: ON";
        }
        else {
            difficulty = "Hardcore: OFF";
        }

    }
    protected void keyPressed(char character, int code) {
        if (this.seedField.isFocused()) {
            this.seedField.keyPressed(character, code);
            this.seed = this.seedField.getText();
        }

    }
}
