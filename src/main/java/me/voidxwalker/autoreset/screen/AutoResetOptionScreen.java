package me.voidxwalker.autoreset.screen;

import me.voidxwalker.autoreset.Atum;
import me.voidxwalker.autoreset.mixin.GeneratorTypeAccessor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.Difficulty;
import org.jetbrains.annotations.Nullable;


public class AutoResetOptionScreen extends Screen{
    private final Screen parent;
    private TextFieldWidget seedField;
    private String seed;
    private int difficulty;
    private int generatorType;
    private boolean structures;
    private boolean bonusChest;

    public AutoResetOptionScreen(@Nullable Screen parent) {
        super(Atum.getTranslation("menu.autoresetTitle","Autoreset Options"));
        this.parent = parent;
    }

    protected void init() {
        this.client.keyboard.setRepeatEvents(true);
        this.seedField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, this.height - 160, 200, 20, Atum.getTranslation("menu.enterSeed","Enter a Seed")) {};
        this.seedField.setText(Atum.seed==null?"":Atum.seed);
        this.seed=Atum.seed;
        this.generatorType=Atum.generatorType;
        this.structures=Atum.structures;
        this.bonusChest=Atum.bonusChest;
        this.difficulty=Atum.difficulty;
        this.seedField.setChangedListener((string) -> this.seed = string);
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 5, this.height - 100, 150, 20, new TranslatableText("options.difficulty"), (buttonWidget) -> {
            this.difficulty = this.difficulty >= 3 ? -1 : this.difficulty + 1;
        }){
            public Text getMessage() {
                if(difficulty==-1){
                    return super.getMessage().shallowCopy().append(": ").append(new TranslatableText("selectWorld.gameMode.hardcore"));
                }
                return super.getMessage().shallowCopy().append(": ").append(Difficulty.byOrdinal(difficulty).getTranslatableName());
            }
        });
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 155, this.height - 100, 150, 20, new TranslatableText("selectWorld.mapType"), (buttonWidget) -> {
            generatorType++;
            if(generatorType>6){
                generatorType=0;
            }
        }){
            public Text getMessage() {
                return super.getMessage().shallowCopy().append(": ").append(GeneratorTypeAccessor.getVALUES().get(generatorType).getDisplayName());
            }
        });

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 155, this.height - 64, 150, 20,  new TranslatableText("selectWorld.mapFeatures"), (buttonWidget) -> {
            this.structures=!structures;
        }){
            public Text getMessage() {
                return super.getMessage().shallowCopy().append(" ").append(String.valueOf(structures));
            }
        });

        this.addDrawableChild(new ButtonWidget(this.width / 2 + 5, this.height - 64, 150, 20, new TranslatableText("selectWorld.bonusItems"), (buttonWidget) -> {
            this.bonusChest=!bonusChest;
        }){
            public Text getMessage() {
                return super.getMessage().shallowCopy().append(" ").append(String.valueOf(bonusChest));
            }
        });

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 155, this.height - 28, 150, 20, Atum.getTranslation("menu.done","Done") , (buttonWidget) -> {
            Atum.seed=this.seed;
            Atum.difficulty=this.difficulty;
            Atum.structures=this.structures;
            Atum.bonusChest=this.bonusChest;
            Atum.generatorType=this.generatorType;
            Atum.saveProperties();
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
        drawStringWithShadow(matrices, this.textRenderer, Atum.getTranslation("menu.enterSeed","Seed (Leave empty for a random Seed)").asString(), this.width / 2 - 100, this.height - 180, -6250336);

        this.seedField.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }

}
