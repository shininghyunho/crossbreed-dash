package com.me.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;

public class ExampleGui extends LightweightGuiDescription {
    public ExampleGui() {
        // Add widgets here
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(300, 200);
    }
}