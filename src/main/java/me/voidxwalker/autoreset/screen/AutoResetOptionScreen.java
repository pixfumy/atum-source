package me.voidxwalker.autoreset.screen;

import me.voidxwalker.autoreset.Atum;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import org.jetbrains.annotations.Nullable;


public class AutoResetOptionScreen extends Screen{
    private final Screen parent;
    private TextFieldWidget seedField;
    private String seed;
    private String difficulty;
    private boolean isHardcore;
    protected String title = "Autoreset Options";
    public AutoResetOptionScreen(@Nullable Screen parent) {
        super();
        this.parent = parent;
    }

    public void init() {
        this.isHardcore= Atum.isHardcore;
        setDifficulty();
        seed= Atum.seed;
        this.seedField = new TextFieldWidget(6969696,client.textRenderer, this.width / 2 - 100, this.height - 160, 200, 20) ;
        this.seedField.setText(Atum.seed==null?"": Atum.seed);
        this.seedField.setFocused(true);
        method_13411(new ButtonWidget(1,this.width / 2 - 75, this.height-100, 150, 20,difficulty){
            public void method_18374(double d, double e) {
                ((AutoResetOptionScreen)MinecraftClient.getInstance().currentScreen).isHardcore =!((AutoResetOptionScreen)MinecraftClient.getInstance().currentScreen).isHardcore;
                setDifficulty();
                this.message=difficulty;
            }
        });
        method_13411(new ButtonWidget(2,this.width / 2 - 155, this.height - 28, 150, 20, I18n.translate("gui.done") ){
            public void method_18374(double d, double e) {
                Atum.seed=seed;
                Atum.isHardcore= ((AutoResetOptionScreen)MinecraftClient.getInstance().currentScreen).isHardcore;
                Atum.saveDifficulty();
                Atum.saveSeed();
                MinecraftClient.getInstance().openScreen(null);
            }
        });
        method_13411(new ButtonWidget(3,this.width / 2 + 5, this.height - 28, 150, 20, I18n.translate("gui.cancel")){
            public void method_18374(double d, double e) {
                MinecraftClient.getInstance().openScreen(null);
            }
        });
    }


public void tick(){
        seedField.tick();
}

    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        drawCenteredString(client.textRenderer, this.title, this.width / 2, this.height - 210, -1);
        this.drawWithShadow( client.textRenderer, "Seed (Leave empty for a random Seed)", this.width / 2 - 100, this.height - 180, -6250336);

        this.seedField.method_18385(mouseX, mouseY, delta);
        super.render(mouseX, mouseY, delta);
    }

    private void setDifficulty() {
        if(this.isHardcore) {
            difficulty = "Hardcore: ON";
        }
        else {
            difficulty = "Hardcore: OFF";
        }

    }
    public boolean keyPressed(int i, int j, int k) {
        if (this.seedField.isFocused()) {
            this.seedField.keyPressed(i,j,k);
            this.seed = this.seedField.getText();
            return true;
        }

        return false;
    }
}
