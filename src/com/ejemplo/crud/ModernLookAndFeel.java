package com.ejemplo.crud;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

public class ModernLookAndFeel extends MetalLookAndFeel {

    private static final Color PRIMARY = new Color(0x4F46E5);
    private static final Color DANGER = new Color(0xDC2626);
    private static final Color NEUTRAL = new Color(0xE6EAF3);
    private static final Color TEXT = new Color(0x1E293B);
    private static final Color SURFACE = new Color(0xF5F7FC);
    private static final Color CARD = new Color(0xFFFFFF);
    private static final Color BORDER = new Color(0xD7DEEB);
    private static final Color HEADER = new Color(0x312E81);
    private static final Color SELECTION = new Color(0xDCE4FF);
    private static final Color FOCUS = new Color(0xA5B4FC);
    private static final Color THUMB = new Color(0xC3CCE0);
    private static final Color GRID = new Color(0xEEF2F8);

    @Override
    public String getName() {
        return "CRUD Moderno";
    }

    @Override
    public String getID() {
        return "CRUDModerno";
    }

    @Override
    public String getDescription() {
        return "Tema moderno con componentes redondeados y paleta indigo.";
    }

    @Override
    public UIDefaults getDefaults() {
        UIDefaults defaults = super.getDefaults();

        FontUIResource regular = new FontUIResource("Segoe UI", Font.PLAIN, 13);
        FontUIResource bold = new FontUIResource("Segoe UI", Font.BOLD, 13);

        defaults.put("Panel.background", SURFACE);
        defaults.put("Panel.foreground", TEXT);
        defaults.put("RootPane.background", SURFACE);
        defaults.put("Frame.background", SURFACE);
        defaults.put("Dialog.background", SURFACE);
        defaults.put("Viewport.background", CARD);
        defaults.put("ScrollPane.background", SURFACE);

        defaults.put("Label.foreground", TEXT);
        defaults.put("Label.font", regular);

        defaults.put("ButtonUI", ModernButtonUI.class.getName());
        defaults.put("Button.background", PRIMARY);
        defaults.put("Button.foreground", CARD);
        defaults.put("Button.font", bold);
        defaults.put("Button.margin", new InsetsUIResource(0, 0, 0, 0));
        defaults.put("Button.border", new BorderUIResource.EmptyBorderUIResource(8, 18, 8, 18));
        defaults.put("Button.rollover", Boolean.TRUE);
        defaults.put("Button.focusPainted", Boolean.TRUE);

        defaults.put("TextFieldUI", ModernTextFieldUI.class.getName());
        defaults.put("TextField.background", CARD);
        defaults.put("TextField.foreground", TEXT);
        defaults.put("TextField.font", regular);
        defaults.put("TextField.border", new BorderUIResource.EmptyBorderUIResource(9, 12, 9, 12));
        defaults.put("TextField.caretForeground", PRIMARY);
        defaults.put("TextField.selectionBackground", SELECTION);
        defaults.put("TextField.selectionForeground", TEXT);

        defaults.put("Table.background", CARD);
        defaults.put("Table.foreground", TEXT);
        defaults.put("Table.font", regular);
        defaults.put("Table.gridColor", GRID);
        defaults.put("Table.selectionBackground", SELECTION);
        defaults.put("Table.selectionForeground", TEXT);
        defaults.put("Table.focusCellBackground", SELECTION);
        defaults.put("Table.focusCellForeground", TEXT);
        defaults.put("Table.cellNoFocusBorder", new BorderUIResource.EmptyBorderUIResource(0, 10, 0, 10));

        defaults.put("TableHeaderUI", ModernTableHeaderUI.class.getName());
        defaults.put("TableHeader.background", HEADER);
        defaults.put("TableHeader.foreground", CARD);
        defaults.put("TableHeader.font", bold);
        defaults.put("TableHeader.cellBorder", new BorderUIResource.EmptyBorderUIResource(8, 12, 8, 12));
        defaults.put("TableHeader.dividerSize", 0);

        defaults.put("ScrollBarUI", ModernScrollBarUI.class.getName());
        defaults.put("ScrollBar.background", SURFACE);
        defaults.put("ScrollBar.track", SURFACE);
        defaults.put("ScrollBar.thumb", THUMB);

        defaults.put("OptionPane.background", SURFACE);
        defaults.put("OptionPane.foreground", TEXT);
        defaults.put("OptionPane.messageForeground", TEXT);
        defaults.put("OptionPane.font", regular);
        defaults.put("OptionPane.messageFont", regular);
        defaults.put("OptionPane.buttonFont", bold);

        defaults.put("ToolTip.background", CARD);
        defaults.put("ToolTip.foreground", TEXT);

        return defaults;
    }

    private static Color mix(Color original, Color target, float amount) {
        int r = Math.round(original.getRed() + (target.getRed() - original.getRed()) * amount);
        int g = Math.round(original.getGreen() + (target.getGreen() - original.getGreen()) * amount);
        int b = Math.round(original.getBlue() + (target.getBlue() - original.getBlue()) * amount);
        return new Color(r, g, b);
    }

    private static String textOf(AbstractButton button) {
        return button.getText() == null ? "" : button.getText().trim();
    }

    private static boolean isSecondary(AbstractButton button) {
        String text = textOf(button);
        return text.equalsIgnoreCase("Cancelar") || text.equalsIgnoreCase("Recargar archivo");
    }

    public static class ModernButtonUI extends BasicButtonUI {
        public static ComponentUI createUI(JComponent component) {
            return new ModernButtonUI();
        }

        @Override
        public void update(Graphics g, JComponent component) {
            paint(g, component);
        }

        @Override
        public void paint(Graphics g, JComponent component) {
            AbstractButton button = (AbstractButton) component;
            ButtonModel model = button.getModel();
            int width = component.getWidth();
            int height = component.getHeight();
            if (width <= 0 || height <= 0) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                Color base = textOf(button).equalsIgnoreCase("Eliminar") ? DANGER
                        : isSecondary(button) ? NEUTRAL : PRIMARY;
                Color background = base;
                if (!model.isEnabled()) {
                    background = mix(base, CARD, 0.55f);
                } else if (model.isPressed()) {
                    background = mix(base, Color.BLACK, 0.14f);
                } else if (model.isRollover()) {
                    background = mix(base, Color.WHITE, 0.12f);
                }

                int arc = Math.min(height, 20);
                g2.setColor(background);
                g2.fillRoundRect(0, 0, width, height, arc, arc);

                if (model.isEnabled() && button.isFocusPainted() && button.hasFocus()) {
                    g2.setColor(FOCUS);
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawRoundRect(2, 2, width - 4, height - 4, arc, arc);
                }

                String text = button.getText();
                if (text != null && !text.isEmpty()) {
                    FontMetrics metrics = g2.getFontMetrics();
                    Rectangle viewRect = new Rectangle(0, 0, width, height);
                    Rectangle iconRect = new Rectangle();
                    Rectangle textRect = new Rectangle();
                    SwingUtilities.layoutCompoundLabel(component, metrics, text, button.getIcon(),
                            SwingConstants.CENTER, SwingConstants.CENTER,
                            SwingConstants.CENTER, SwingConstants.CENTER,
                            viewRect, iconRect, textRect, button.getIconTextGap());

                    Color foreground = isSecondary(button) ? TEXT : CARD;
                    if (!model.isEnabled()) {
                        foreground = mix(foreground, background, 0.45f);
                    }
                    g2.setColor(foreground);
                    int baseline = textRect.y + (textRect.height - metrics.getHeight()) / 2 + metrics.getAscent();
                    g2.drawString(text, textRect.x, baseline);
                }
            } finally {
                g2.dispose();
            }
        }
    }

    public static class ModernTextFieldUI extends BasicTextFieldUI {
        public static ComponentUI createUI(JComponent component) {
            return new ModernTextFieldUI();
        }

        @Override
        public void update(Graphics g, JComponent component) {
            paint(g, component);
        }

        @Override
        protected void paintBackground(Graphics g) {
            JTextComponent editor = getComponent();
            int width = editor.getWidth();
            int height = editor.getHeight();
            if (width <= 0 || height <= 0) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int arc = Math.min(14, height);
                g2.setColor(editor.getBackground());
                g2.fillRoundRect(0, 0, width, height, arc, arc);
                g2.setColor(editor.hasFocus() ? PRIMARY : BORDER);
                g2.setStroke(new BasicStroke(editor.hasFocus() ? 2f : 1f));
                g2.drawRoundRect(1, 1, width - 2, height - 2, arc, arc);
            } finally {
                g2.dispose();
            }
        }
    }

    public static class ModernTableHeaderUI extends BasicTableHeaderUI {
        public static ComponentUI createUI(JComponent component) {
            return new ModernTableHeaderUI();
        }

        @Override
        public void paint(Graphics g, JComponent component) {
            super.paint(g, component);
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int width = component.getWidth();
                int height = component.getHeight();
                Area outside = new Area(new Rectangle(0, 0, width, height));
                outside.subtract(new Area(new RoundRectangle2D.Float(0, 0, width, height + 18, 18, 18)));
                Component parent = component.getParent();
                g2.setColor(parent != null ? parent.getBackground() : SURFACE);
                g2.fill(outside);
            } finally {
                g2.dispose();
            }
        }
    }

    public static class ModernScrollBarUI extends BasicScrollBarUI {
        public static ComponentUI createUI(JComponent component) {
            return new ModernScrollBarUI();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent component, Rectangle trackBounds) {
            g.setColor(SURFACE);
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }

        @Override
        protected void paintThumb(Graphics g, JComponent component, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(THUMB);
                int arc = Math.min(12, Math.min(thumbBounds.width, thumbBounds.height));
                if (thumbBounds.width < thumbBounds.height) {
                    g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y, thumbBounds.width - 4, thumbBounds.height, arc, arc);
                } else {
                    g2.fillRoundRect(thumbBounds.x, thumbBounds.y + 2, thumbBounds.width, thumbBounds.height - 4, arc, arc);
                }
            } finally {
                g2.dispose();
            }
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return hiddenButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return hiddenButton();
        }

        private static JButton hiddenButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            button.setFocusable(false);
            button.setOpaque(false);
            return button;
        }
    }
}
