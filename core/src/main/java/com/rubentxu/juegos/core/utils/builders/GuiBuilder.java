package com.rubentxu.juegos.core.utils.builders;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.rubentxu.juegos.core.controladores.WorldController;
import com.rubentxu.juegos.core.inputs.GuiInput;
import com.rubentxu.juegos.core.servicios.Styles;


public class GuiBuilder {

    public static final void buildGui (Stage stage,Styles styles,WorldController controller) {
        stage.clear();
        Table layerControlsLeft =  new Table();
        layerControlsLeft.left().bottom();

        layerControlsLeft.row().height(stage.getHeight()*2/3);
        TextButton btnUpLeft = new TextButton("U",styles.skin,"controls");
        layerControlsLeft.add(btnUpLeft).width(stage.getWidth()/10).expandY().fill();
        GuiInput input=new GuiInput(controller,stage);
        btnUpLeft.addListener(input);

        layerControlsLeft.row().height(stage.getHeight()*1/3);
        TextButton btnLeft = new TextButton( "L",styles.skin,"controls");
        layerControlsLeft.add(btnLeft).width(stage.getWidth()/10).expandY().fill();
        btnLeft.addListener(input);

        Table layerControlsRight=  new Table();
        layerControlsRight.right().bottom();

        layerControlsRight.row().height(stage.getHeight()*2/3);

        TextButton btnUpRight = new TextButton("U",styles.skin,"controls");
        layerControlsRight.add(btnUpRight).width(stage.getWidth()/10).expandY().fill();
        btnUpRight.addListener(input);

        layerControlsRight.row().height(stage.getHeight()*1/3);

        TextButton btnRight = new TextButton( "R",styles.skin,"controls");
        layerControlsRight.add(btnRight).width(stage.getWidth()/10).expandY().fill();
        btnRight.addListener(input);

        Stack stack = new Stack();
        stack.setSize(stage.getWidth() ,stage.getHeight() );
        System.out.println("Stack size: " + stack.getWidth() + "--" + stack.getHeight());
        stack.add(layerControlsLeft);
        stack.add(layerControlsRight);
        stage.addActor(stack);

    }
}
