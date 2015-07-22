/* JMathField.java
 * =========================================================================
 * This file is part of the Mirai Math TN - http://mirai.sourceforge.net
 *
 * Copyright (C) 2008-2009 Bea Petrovicova
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 *
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 *
 */
package com.himamis.retex.editor.share.editor;

import com.himamis.retex.editor.share.algebra.TeXSerializer;
import com.himamis.retex.editor.share.controller.MathInputController;
import com.himamis.retex.editor.share.event.ClickListener;
import com.himamis.retex.editor.share.event.FocusListener;
import com.himamis.retex.editor.share.event.KeyEvent;
import com.himamis.retex.editor.share.event.KeyListener;
import com.himamis.retex.editor.share.model.MathFormula;
import com.himamis.retex.editor.share.model.MathSequence;
import com.himamis.retex.renderer.share.TeXConstants;
import com.himamis.retex.renderer.share.TeXFormula;
import com.himamis.retex.renderer.share.TeXIcon;

/**
 * This class is a Math Field. Displays and allows to edit single formula.
 *
 * @author Bea Petrovicova
 */
public class MathFieldInternal {

    private TeXIcon renderer;
    private TeXSerializer serializer;
    private MathField mathField;
    private ClickListener clickListener = new ClickListener() {

        public void onClick() {
            mathField.requestViewFocus();

        }
    };
    private MathInputController controller = new MathInputController() {

        public void update() {
            MathFieldInternal.this.update(currentField, currentOffset);
        }
    };
    FocusListener focusListener = new FocusListener() {

        public void onFocusLost() {
            update();
        }

        public void onFocusGained() {
            controller.update();
        }
    };
    private KeyListener keyListener = new KeyListener() {
        public void onKeyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            int modifiers = e.getKeyModifiers();
            // System.out.println("key_released: "+keyCode+", "+modifiers);
            controller.keyPressed(keyCode, modifiers);
        }

        public void onKeyReleased(KeyEvent e) {
            // int keyCode = e.getKeyCode();
            // int modifiers = e.getModifiersEx();
            // System.out.println("key_released: "+keyCode+", "+modifiers);
            // controller.keyReleased(keyCode,modifiers);
        }

        public void onKeyTyped(KeyEvent e) {
            char ch = e.getUnicodeKeyChar();
            // System.out.println("key_typed: "+ch+", "+modifiers);
            controller.keyTyped(ch);
        }
    };

    private float size;

    public MathFieldInternal() {
        serializer = new TeXSerializer();
    }

    public void setMathField(MathField mathField) {
        this.mathField = mathField;
        setupMathField();
    }

    public void setSize(float size) {
        this.size = size;
    }

    public MathInputController getController() {
        return controller;
    }

    public MathFormula getFormula() {
        return controller.getFormula();
    }

    public void setFormula(MathFormula formula) {
        controller.setFormula(formula);
        updateFormula(null, 0);
    }

    private void setupMathField() {
        mathField.setFocusListener(focusListener);
        mathField.setClickListener(clickListener);
        mathField.setKeyListener(keyListener);
    }

    private void updateFormula(MathSequence currentField,
                               int currentOffset) {
        String serializedFormula = serializer.serialize(
                controller.getFormula(), currentField, currentOffset);

        TeXFormula texFormula = new TeXFormula(serializedFormula);
        renderer = texFormula.new TeXIconBuilder()
                .setStyle(TeXConstants.STYLE_DISPLAY).setSize(size).build();
        mathField.setTeXIcon(renderer);
    }

    private void update(MathSequence currentField, int currentOffset) {
        if (mathField.hasParent()) {
            updateFormula(currentField, currentOffset);
            mathField.requestLayout();
            mathField.repaint();

        }
    }

    public void update() {
        update(null, 0);
    }

}
