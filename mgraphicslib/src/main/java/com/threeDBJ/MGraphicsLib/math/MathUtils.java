/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.threeDBJ.MGraphicsLib.math;

/** Utility and fast math functions.
 * <p>
 * Thanks to Riven on JavaGaming.org for the basis of sin/cos/atan2/floor/ceil.
 * @author Nathan Sweet */
public class MathUtils {
	static public final float PI = 3.1415927f;

	static private final int SIN_BITS = 13; // Adjust for accuracy.
	static private final int SIN_MASK = ~(-1 << SIN_BITS);
	static private final int SIN_COUNT = SIN_MASK + 1;

	static private final float radFull = PI * 2;
	static private final float degFull = 360;
	static private final float radToIndex = SIN_COUNT / radFull;
	static private final float degToIndex = SIN_COUNT / degFull;

	static public final float radiansToDegrees = 180f / PI;
	static public final float radDeg = radiansToDegrees;
	static public final float degreesToRadians = PI / 180;
	static public final float degRad = degreesToRadians;

	static private class Sin {
		static final float[] table = new float[SIN_COUNT];
		static {
			for (int i = 0; i < SIN_COUNT; i++)
				table[i] = (float)Math.sin((i + 0.5f) / SIN_COUNT * radFull);
			for (int i = 0; i < 360; i += 90)
				table[(int)(i * degToIndex) & SIN_MASK] = (float)Math.sin(i * degreesToRadians);
		}
	}

	static private class Cos {
		static final float[] table = new float[SIN_COUNT];
		static {
			for (int i = 0; i < SIN_COUNT; i++)
				table[i] = (float)Math.cos((i + 0.5f) / SIN_COUNT * radFull);
			for (int i = 0; i < 360; i += 90)
				table[(int)(i * degToIndex) & SIN_MASK] = (float)Math.cos(i * degreesToRadians);
		}
	}

	/** Returns the sine in radians. */
	static public final float sin (float radians) {
		return Sin.table[(int)(radians * radToIndex) & SIN_MASK];
	}

	/** Returns the cosine in radians. */
	static public final float cos (float radians) {
		return Cos.table[(int)(radians * radToIndex) & SIN_MASK];
	}

	/** Returns the sine in radians. */
	static public final float sinDeg (float degrees) {
		return Sin.table[(int)(degrees * degToIndex) & SIN_MASK];
	}

	/** Returns the cosine in radians. */
	static public final float cosDeg (float degrees) {
		return Cos.table[(int)(degrees * degToIndex) & SIN_MASK];
	}

	// ---

	static private final int ATAN2_BITS = 7; // Adjust for accuracy.
	static private final int ATAN2_BITS2 = ATAN2_BITS << 1;
	static private final int ATAN2_MASK = ~(-1 << ATAN2_BITS2);
	static private final int ATAN2_COUNT = ATAN2_MASK + 1;
	static final int ATAN2_DIM = (int)Math.sqrt(ATAN2_COUNT);
	static private final float INV_ATAN2_DIM_MINUS_1 = 1.0f / (ATAN2_DIM - 1);

	static private class Atan2 {
		static final float[] table = new float[ATAN2_COUNT];
		static {
			for (int i = 0; i < ATAN2_DIM; i++) {
				for (int j = 0; j < ATAN2_DIM; j++) {
					float x0 = (float)i / ATAN2_DIM;
					float y0 = (float)j / ATAN2_DIM;
					table[j * ATAN2_DIM + i] = (float)Math.atan2(y0, x0);
				}
			}
		}
	}

	/** Returns atan2 in radians from a lookup table. */
	static public final float atan2 (float y, float x) {
		float add, mul;
		if (x < 0) {
			if (y < 0) {
				y = -y;
				mul = 1;
			} else
				mul = -1;
			x = -x;
			add = -PI;
		} else {
			if (y < 0) {
				y = -y;
				mul = -1;
			} else
				mul = 1;
			add = 0;
		}
		float invDiv = 1 / ((x < y ? y : x) * INV_ATAN2_DIM_MINUS_1);
		int xi = (int)(x * invDiv);
		int yi = (int)(y * invDiv);
		return (Atan2.table[yi * ATAN2_DIM + xi] + add) * mul;
	}
}