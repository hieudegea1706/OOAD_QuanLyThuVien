/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package components;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import utils.UITheme;

public class ModernTextField extends JTextField {
    public ModernTextField() {
        setFont(UITheme.FONT_NORMAL);
        setForeground(UITheme.TEXT_DARK);
        setBackground(Color.WHITE);
        setCaretColor(UITheme.PRIMARY);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
    }
}
