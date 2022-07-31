package me.voidxwalker.autoreset.screen;

import me.voidxwalker.autoreset.Atum;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.level.LevelGeneratorType;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;


public class AutoResetOptionScreen extends Screen{
    private final Screen parent;
    private TextFieldWidget seedField;
    private String seed;
    private boolean isHardcore;
    private int generatorType;
    private boolean structures;
    private boolean bonusChest;

    public AutoResetOptionScreen(@Nullable Screen parent) {
        super(Atum.getTranslation("menu.autoresetTitle","Autoreset Options"));
        this.parent = parent;
    }

    protected void init() {
        this.minecraft.keyboard.enableRepeatEvents(true);
        this.isHardcore=Atum.difficulty==-1;
        this.seedField = new TextFieldWidget(this.minecraft.textRenderer, this.width / 2 - 100, this.height - 160, 200, 20, Atum.getTranslation("menu.enterSeed","Enter a Seed").asString()) {};
        this.seedField.setText(Atum.seed==null?"":Atum.seed);
        this.seed=Atum.seed;
        this.generatorType=Atum.generatorType;
        this.structures=Atum.structures;
        this.bonusChest=Atum.bonusChest;
        this.seedField.setChangedListener((string) -> this.seed = string);
        this.addButton(new ButtonWidget(this.width / 2 + 5, this.height - 100, 150, 20, new LiteralText("Is Hardcore: "+isHardcore).asString(), (buttonWidget) -> {
            this.isHardcore=!this.isHardcore;
            buttonWidget.setMessage("Is Hardcore: "+isHardcore);
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 155, this.height - 100, 150, 20, new TranslatableText("selectWorld.mapType").asString()+" "+LevelGeneratorType.TYPES[generatorType].getTranslationKey(), (buttonWidget) -> {
            generatorType++;
            if(generatorType>5){
                generatorType=0;
            }
            buttonWidget.setMessage(new TranslatableText("selectWorld.mapType").asString()+" "+LevelGeneratorType.TYPES[generatorType].getTranslationKey());
        }));

        this.addButton(new ButtonWidget(this.width / 2 - 155, this.height - 64, 150, 20,  new TranslatableText("selectWorld.mapFeatures").asString()+" "+structures, (buttonWidget) -> {
            this.structures=!structures;
            buttonWidget.setMessage(new TranslatableText("selectWorld.mapFeatures").asString()+" "+structures);
        }));

        this.addButton(new ButtonWidget(this.width / 2 + 5, this.height - 64, 150, 20, new TranslatableText("selectWorld.bonusItems").asString()+" "+bonusChest, (buttonWidget) -> {
            this.bonusChest=!bonusChest;
            buttonWidget.setMessage( new TranslatableText("selectWorld.bonusItems").asString()+" "+bonusChest);
        }));

        this.addButton(new ButtonWidget(this.width / 2 - 155, this.height - 28, 150, 20, Atum.getTranslation("menu.done","Done").asString(), (buttonWidget) -> {
            Atum.seed=this.seed;
            Atum.difficulty=this.isHardcore?-1:0;
            Atum.structures=this.structures;
            Atum.bonusChest=this.bonusChest;
            Atum.generatorType=this.generatorType;
            try {
                Atum.saveProperties();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.minecraft.openScreen(this.parent);
        }));
        this.addButton(new ButtonWidget(this.width / 2 + 5, this.height - 28, 150, 20, I18n.translate("gui.cancel"), (buttonWidget) -> this.minecraft.openScreen(this.parent)));
        this.children.add(this.seedField);
        this.setInitialFocus(this.seedField);
    }

    public void removed() {
        this.minecraft.keyboard.enableRepeatEvents(false);
    }

    public void onClose() {
        this.minecraft.openScreen(this.parent);
    }

    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        drawCenteredString(minecraft.textRenderer, this.title.asString(), this.width / 2, this.height - 210, -1);
        drawString( minecraft.textRenderer, Atum.getTranslation("menu.enterSeed","Seed (Leave empty for a random Seed)").asString(), this.width / 2 - 100, this.height - 180, -6250336);

        this.seedField.render( mouseX, mouseY, delta);
        super.render(mouseX, mouseY, delta);
    }


}
