package tools;

import java.awt.Color;

enum ColorEnum {
	PROCESS_RED(Color.RED, 16), PROCESS_GREEN(Color.GREEN, 8), PROCESS_BLUE(Color.BLUE, 0);

	final Color color;
	final int offset;

	ColorEnum(Color color, int offset) {
		this.color = color;
		this.offset = offset;
	}
	
	static ColorEnum findColor(Color color) {
		for (ColorEnum c : ColorEnum.values()) {
			if (c.color == color) {
				return c;
			}
		}
		throw new RuntimeException("Unexpected color: " + color);
	}
}
