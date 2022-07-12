package me.voidxwalker.autoreset.screen;

import me.voidxwalker.autoreset.Atum;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.level.LevelGeneratorType;
import org.jetbrains.annotations.Nullable;


public class AutoResetOptionScreen extends Screen{
    private final Screen parent;
    private TextFieldWidget seedField;
    private String seed;
    private boolean isHardcore;
    private int generatorType;
    private boolean structures;
    private String title;
    private boolean bonusChest;

    public AutoResetOptionScreen(@Nullable Screen parent) {
        super();
        title=Atum.getTranslation("menu.autoresetTitle","Autoreset Options").asString();
        this.parent = parent;
    }

    protected void init() {
        this.client.field_19946.method_18191(true);
        this.isHardcore=Atum.difficulty==-1;
        this.seedField = new TextFieldWidget(350,this.client.textRenderer, this.width / 2 - 100, this.height - 160, 200, 20) {};
        this.seedField.setText(Atum.seed==null?"":Atum.seed);
        this.seed=Atum.seed;
        this.generatorType=Atum.generatorType;
        this.structures=Atum.structures;
        this.bonusChest=Atum.bonusChest;

        this.method_13411(new ButtonWidget(340,this.width / 2 + 5, this.height - 100, 150, 20, new LiteralText("Is Hardcore: "+isHardcore).asString()){
            public void method_18374(double d, double e) {
                isHardcore=!isHardcore;
                this.message=("Is Hardcore: "+isHardcore);
            }

        });
        this.method_13411(new ButtonWidget(341,this.width / 2 - 155, this.height - 100, 150, 20, new TranslatableText("selectWorld.mapType").asString()+" "+ LevelGeneratorType.TYPES[generatorType].getTranslationKey()) {
            public void method_18374(double d, double e) {
                generatorType++;
                if(generatorType>5){
                    generatorType=0;
                }
                this.message=(new TranslatableText("selectWorld.mapType").asString()+" "+LevelGeneratorType.TYPES[generatorType].getTranslationKey());

            }
        });

        this.method_13411(new ButtonWidget(342,this.width / 2 - 155, this.height - 64, 150, 20,  new TranslatableText("selectWorld.mapFeatures").asString()+" "+structures) {
            public void method_18374(double d, double e) {
                structures=!structures;
                this.message=(new TranslatableText("selectWorld.mapFeatures").asString()+" "+structures);
            }


        });

        this.method_13411(new ButtonWidget(344,this.width / 2 + 5, this.height - 64, 150, 20, new TranslatableText("selectWorld.bonusItems").asString()+" "+bonusChest){
            public void method_18374(double d, double e) {
                bonusChest=!bonusChest;
                this.message=( new TranslatableText("selectWorld.bonusItems").asString()+" "+bonusChest);
            }

        });

        this.method_13411(new ButtonWidget(345,this.width / 2 - 155, this.height - 28, 150, 20, Atum.getTranslation("menu.done","Done").asString()){
            public void method_18374(double d, double e) {
                Atum.seed=seed;
                Atum.difficulty=isHardcore?-1:0;
                Atum.structures=structures;
                Atum.bonusChest=bonusChest;
                Atum.generatorType=generatorType;
                Atum.saveProperties();
                client.openScreen(parent);
            }

        });
        this.method_13411(new ButtonWidget(343,this.width / 2 + 5, this.height - 28, 150, 20, I18n.translate("gui.cancel")){
            public void method_18374(double d, double e) {
                client.openScreen(parent);
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


    public boolean keyPressed(int i, int j, int k) {
        if (this.seedField.isFocused()) {
            this.seedField.keyPressed(i,j,k);
            this.seed = this.seedField.getText();
            return true;
        }

        return false;
    }
}
