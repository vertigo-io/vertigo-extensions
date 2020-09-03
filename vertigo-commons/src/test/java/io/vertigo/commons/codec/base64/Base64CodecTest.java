/**
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2020, Vertigo.io, team@vertigo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.commons.codec.base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Base64;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import io.vertigo.commons.codec.AbstractCodecTest;
import io.vertigo.commons.codec.Codec;
import io.vertigo.commons.codec.CodecManager;

/**
 * Test du codec de base64.
 * @author pchretien
 */
public final class Base64CodecTest extends AbstractCodecTest<byte[], String> {
	/**
	 * "Les sanglots longs des .." encodé en base64.
	 */
	private static final String TEXT64 = "TGVzIHNhbmdsb3RzIGxvbmdzIGRlcyB2aW9sb25zIGRlIGwnYXV0b21uZSBibGVzc2VudCBtb24gY29ldXIgZCd1bmUgbGFuZ3VldXIgbW9ub3RvbmUu";

	/**
	 * bytes[] d'un objet du Projet CMS de Jonathan Tiret et Hugo Garcia-Cotte
	 * Ces 3 bytes se différencient entre autre par le résultat de leur division modulo 3
	 */
	private static final byte[] BYTECMS_MOD_2 = new byte[] { -84, -19, 0, 5, 115, 114, 0, 55, 99, 111, 109, 46, 107, 108, 101, 101, 103, 114, 111, 117, 112, 46, 97, 109, 97, 122, 105, 110, 103, 99, 97, 114, 100, 115, 46, 107, 101, 114, 110, 101, 108, 46, 99, 97, 114, 100, 46, 67, 97, 114, 100, 83, 101, 114, 105, 97, 108, 105, 122, 97, 98, 108, 101, -3, -79, -30, 45, 27, -39, -83, -35, 2, 0, 6, 76, 0, 11, 100, 101, 115, 99, 114, 105, 112, 116, 105, 111, 110, 116, 0, 18, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 76, 0, 4, 110, 97, 109, 101, 113, 0, 126, 0, 1, 76, 0, 10, 111, 116, 104, 101, 114, 68, 97, 116, 97, 115, 116, 0, 19, 76, 106, 97, 118, 97, 47, 117, 116, 105, 108, 47, 72, 97, 115, 104, 77, 97, 112, 59, 76, 0, 4, 116, 121, 112, 101, 116, 0, 49, 76, 99, 111, 109, 47, 107, 108, 101, 101, 103, 114, 111, 117, 112, 47, 97, 109, 97, 122, 105, 110, 103, 99, 97, 114, 100, 115, 47, 107, 101, 114, 110, 101, 108, 47, 99, 97, 114, 100, 47, 67, 97,
			114, 100, 84, 121, 112, 101, 59, 76, 0, 3, 117, 114, 105, 116, 0, 44, 76, 99, 111, 109, 47, 107, 108, 101, 101, 103, 114, 111, 117, 112, 47, 97, 109, 97, 122, 105, 110, 103, 99, 97, 114, 100, 115, 47, 107, 101, 114, 110, 101, 108, 47, 99, 97, 114, 100, 47, 85, 82, 73, 59, 76, 0, 3, 117, 114, 108, 113, 0, 126, 0, 1, 120, 112, 116, 0, 16, 77, 111, 110, 32, 103, 114, 97, 110, 100, 32, 102, 114, -61, -88, 114, 101, 116, 0, 13, 65, 108, 98, 101, 114, 116, 32, 71, 73, 82, 65, 82, 68, 115, 114, 0, 17, 106, 97, 118, 97, 46, 117, 116, 105, 108, 46, 72, 97, 115, 104, 77, 97, 112, 5, 7, -38, -63, -61, 22, 96, -47, 3, 0, 2, 70, 0, 10, 108, 111, 97, 100, 70, 97, 99, 116, 111, 114, 73, 0, 9, 116, 104, 114, 101, 115, 104, 111, 108, 100, 120, 112, 63, 64, 0, 0, 0, 0, 0, 12, 119, 8, 0, 0, 0, 16, 0, 0, 0, 8, 116, 0, 9, 102, 97, 120, 78, 117, 109, 98, 101, 114, 116, 0, 10, 48, 49, 50, 49, 53, 53, 54, 53, 56, 53, 116, 0, 11, 112, 104, 111, 110, 101, 78, 117, 109, 98, 101, 114, 116, 0, 10,
			48, 54, 50, 53, 53, 49, 53, 54, 51, 50, 116, 0, 5, 112, 84, 121, 112, 101, 126, 114, 0, 74, 99, 111, 109, 46, 107, 108, 101, 101, 103, 114, 111, 117, 112, 46, 97, 109, 97, 122, 105, 110, 103, 99, 97, 114, 100, 115, 46, 112, 108, 117, 103, 105, 110, 115, 46, 115, 97, 109, 112, 108, 101, 112, 108, 117, 103, 105, 110, 46, 99, 97, 114, 100, 46, 67, 97, 114, 100, 80, 101, 111, 112, 108, 101, 36, 80, 101, 114, 115, 111, 110, 84, 121, 112, 101, 0, 0, 0, 0, 0, 0, 0, 0, 18, 0, 0, 120, 114, 0, 14, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 69, 110, 117, 109, 0, 0, 0, 0, 0, 0, 0, 0, 18, 0, 0, 120, 112, 116, 0, 6, 70, 65, 77, 73, 76, 89, 116, 0, 8, 112, 83, 117, 114, 110, 97, 109, 101, 116, 0, 6, 71, 73, 82, 65, 82, 68, 116, 0, 5, 112, 78, 97, 109, 101, 116, 0, 6, 65, 108, 98, 101, 114, 116, 116, 0, 5, 101, 109, 97, 105, 108, 116, 0, 12, 106, 112, 64, 101, 109, 97, 105, 108, 46, 99, 111, 109, 116, 0, 12, 112, 68, 101, 115, 99, 114, 105, 112, 116, 105, 111, 110, 116, 0, 46, 77, 111,
			110, 32, 103, 114, 97, 110, 100, 32, 102, 114, -61, -88, 114, 101, 44, 32, 105, 108, 32, 97, 32, 53, 32, 97, 110, 115, 32, 100, 101, 32, 112, 108, 117, 115, 32, 113, 117, 101, 32, 109, 111, 105, 32, 33, 116, 0, 3, 97, 103, 101, 115, 114, 0, 17, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 73, 110, 116, 101, 103, 101, 114, 18, -30, -96, -92, -9, -127, -121, 56, 2, 0, 1, 73, 0, 5, 118, 97, 108, 117, 101, 120, 114, 0, 16, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 78, 117, 109, 98, 101, 114, -122, -84, -107, 29, 11, -108, -32, -117, 2, 0, 0, 120, 112, 0, 0, 0, 41, 120, 126, 114, 0, 62, 99, 111, 109, 46, 107, 108, 101, 101, 103, 114, 111, 117, 112, 46, 97, 109, 97, 122, 105, 110, 103, 99, 97, 114, 100, 115, 46, 112, 108, 117, 103, 105, 110, 115, 46, 115, 97, 109, 112, 108, 101, 112, 108, 117, 103, 105, 110, 46, 99, 97, 114, 100, 46, 67, 97, 114, 100, 84, 121, 112, 101, 115, 0, 0, 0, 0, 0, 0, 0, 0, 18, 0, 0, 120, 113, 0, 126, 0, 16, 116, 0, 6, 80, 69, 79, 80, 76, 69, 115,
			114, 0, 42, 99, 111, 109, 46, 107, 108, 101, 101, 103, 114, 111, 117, 112, 46, 97, 109, 97, 122, 105, 110, 103, 99, 97, 114, 100, 115, 46, 107, 101, 114, 110, 101, 108, 46, 99, 97, 114, 100, 46, 85, 82, 73, -86, -19, -117, 77, 78, 119, 45, 38, 2, 0, 1, 76, 0, 4, 117, 117, 105, 100, 116, 0, 16, 76, 106, 97, 118, 97, 47, 117, 116, 105, 108, 47, 85, 85, 73, 68, 59, 120, 112, 115, 114, 0, 14, 106, 97, 118, 97, 46, 117, 116, 105, 108, 46, 85, 85, 73, 68, -68, -103, 3, -9, -104, 109, -123, 47, 2, 0, 2, 74, 0, 12, 108, 101, 97, 115, 116, 83, 105, 103, 66, 105, 116, 115, 74, 0, 11, 109, 111, 115, 116, 83, 105, 103, 66, 105, 116, 115, 120, 112, -104, -3, -10, 33, 75, 39, 68, -40, -47, -117, -114, 15, -94, 124, 50, 125, 116, 0, 20, 104, 116, 116, 112, 58, 47, 47, 119, 119, 119, 46, 103, 111, 111, 103, 108, 101, 46, 102, 114 };
	private static final byte[] BYTECMS_MOD_1 = new byte[] { -84, -19, 0, 5, 115, 114, 0, 55, 99, 111, 109, 46, 107, 108, 101, 101, 103, 114, 111, 117, 112, 46, 97, 109, 97, 122, 105, 110, 103, 99, 97, 114, 100, 115, 46, 107, 101, 114, 110, 101, 108, 46, 99, 97, 114, 100, 46, 67, 97, 114, 100, 83, 101, 114, 105, 97, 108, 105, 122, 97, 98, 108, 101, -3, -79, -30, 45, 27, -39, -83, -35, 2, 0, 6, 76, 0, 11, 100, 101, 115, 99, 114, 105, 112, 116, 105, 111, 110, 116, 0, 18, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 76, 0, 4, 110, 97, 109, 101, 113, 0, 126, 0, 1, 76, 0, 10, 111, 116, 104, 101, 114, 68, 97, 116, 97, 115, 116, 0, 19, 76, 106, 97, 118, 97, 47, 117, 116, 105, 108, 47, 72, 97, 115, 104, 77, 97, 112, 59, 76, 0, 4, 116, 121, 112, 101, 116, 0, 49, 76, 99, 111, 109, 47, 107, 108, 101, 101, 103, 114, 111, 117, 112, 47, 97, 109, 97, 122, 105, 110, 103, 99, 97, 114, 100, 115, 47, 107, 101, 114, 110, 101, 108, 47, 99, 97, 114, 100, 47, 67, 97,
			114, 100, 84, 121, 112, 101, 59, 76, 0, 3, 117, 114, 105, 116, 0, 44, 76, 99, 111, 109, 47, 107, 108, 101, 101, 103, 114, 111, 117, 112, 47, 97, 109, 97, 122, 105, 110, 103, 99, 97, 114, 100, 115, 47, 107, 101, 114, 110, 101, 108, 47, 99, 97, 114, 100, 47, 85, 82, 73, 59, 76, 0, 3, 117, 114, 108, 113, 0, 126, 0, 1, 120, 112, 116, 0, 18, 85, 110, 32, 105, 110, 99, 111, 110, 110, 117, 32, 109, 97, 114, 114, 97, 110, 116, 116, 0, 19, 65, 110, 116, 104, 111, 110, 121, 32, 68, 69, 32, 76, 65, 32, 67, 82, 79, 73, 88, 115, 114, 0, 17, 106, 97, 118, 97, 46, 117, 116, 105, 108, 46, 72, 97, 115, 104, 77, 97, 112, 5, 7, -38, -63, -61, 22, 96, -47, 3, 0, 2, 70, 0, 10, 108, 111, 97, 100, 70, 97, 99, 116, 111, 114, 73, 0, 9, 116, 104, 114, 101, 115, 104, 111, 108, 100, 120, 112, 63, 64, 0, 0, 0, 0, 0, 12, 119, 8, 0, 0, 0, 16, 0, 0, 0, 8, 116, 0, 9, 102, 97, 120, 78, 117, 109, 98, 101, 114, 116, 0, 10, 48, 49, 50, 49, 53, 53, 54, 53, 56, 53, 116, 0, 11, 112, 104, 111, 110, 101, 78, 117,
			109, 98, 101, 114, 116, 0, 10, 48, 54, 50, 53, 53, 49, 53, 54, 51, 50, 116, 0, 5, 112, 84, 121, 112, 101, 126, 114, 0, 74, 99, 111, 109, 46, 107, 108, 101, 101, 103, 114, 111, 117, 112, 46, 97, 109, 97, 122, 105, 110, 103, 99, 97, 114, 100, 115, 46, 112, 108, 117, 103, 105, 110, 115, 46, 115, 97, 109, 112, 108, 101, 112, 108, 117, 103, 105, 110, 46, 99, 97, 114, 100, 46, 67, 97, 114, 100, 80, 101, 111, 112, 108, 101, 36, 80, 101, 114, 115, 111, 110, 84, 121, 112, 101, 0, 0, 0, 0, 0, 0, 0, 0, 18, 0, 0, 120, 114, 0, 14, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 69, 110, 117, 109, 0, 0, 0, 0, 0, 0, 0, 0, 18, 0, 0, 120, 112, 116, 0, 9, 65, 78, 79, 78, 89, 77, 79, 85, 83, 116, 0, 8, 112, 83, 117, 114, 110, 97, 109, 101, 116, 0, 11, 68, 69, 32, 76, 65, 32, 67, 82, 79, 73, 88, 116, 0, 5, 112, 78, 97, 109, 101, 116, 0, 7, 65, 110, 116, 104, 111, 110, 121, 116, 0, 5, 101, 109, 97, 105, 108, 116, 0, 12, 106, 112, 64, 101, 109, 97, 105, 108, 46, 99, 111, 109, 116, 0, 12, 112, 68,
			101, 115, 99, 114, 105, 112, 116, 105, 111, 110, 116, 0, 60, 85, 110, 101, 32, 112, 101, 114, 115, 111, 110, 110, 101, 32, 113, 117, 101, 32, 106, 101, 32, 110, 101, 32, 99, 111, 110, 110, 97, 105, 115, 32, 112, 97, 115, 32, 109, 97, 105, 115, 32, 113, 117, 105, 32, -61, -96, 32, 108, 39, 97, 105, 114, 32, 115, 121, 109, 112, 97, 32, 33, 116, 0, 3, 97, 103, 101, 115, 114, 0, 17, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 73, 110, 116, 101, 103, 101, 114, 18, -30, -96, -92, -9, -127, -121, 56, 2, 0, 1, 73, 0, 5, 118, 97, 108, 117, 101, 120, 114, 0, 16, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 78, 117, 109, 98, 101, 114, -122, -84, -107, 29, 11, -108, -32, -117, 2, 0, 0, 120, 112, 0, 0, 0, 65, 120, 126, 114, 0, 62, 99, 111, 109, 46, 107, 108, 101, 101, 103, 114, 111, 117, 112, 46, 97, 109, 97, 122, 105, 110, 103, 99, 97, 114, 100, 115, 46, 112, 108, 117, 103, 105, 110, 115, 46, 115, 97, 109, 112, 108, 101, 112, 108, 117, 103, 105, 110, 46, 99, 97, 114, 100, 46, 67,
			97, 114, 100, 84, 121, 112, 101, 115, 0, 0, 0, 0, 0, 0, 0, 0, 18, 0, 0, 120, 113, 0, 126, 0, 16, 116, 0, 6, 80, 69, 79, 80, 76, 69, 115, 114, 0, 42, 99, 111, 109, 46, 107, 108, 101, 101, 103, 114, 111, 117, 112, 46, 97, 109, 97, 122, 105, 110, 103, 99, 97, 114, 100, 115, 46, 107, 101, 114, 110, 101, 108, 46, 99, 97, 114, 100, 46, 85, 82, 73, -86, -19, -117, 77, 78, 119, 45, 38, 2, 0, 1, 76, 0, 4, 117, 117, 105, 100, 116, 0, 16, 76, 106, 97, 118, 97, 47, 117, 116, 105, 108, 47, 85, 85, 73, 68, 59, 120, 112, 115, 114, 0, 14, 106, 97, 118, 97, 46, 117, 116, 105, 108, 46, 85, 85, 73, 68, -68, -103, 3, -9, -104, 109, -123, 47, 2, 0, 2, 74, 0, 12, 108, 101, 97, 115, 116, 83, 105, 103, 66, 105, 116, 115, 74, 0, 11, 109, 111, 115, 116, 83, 105, 103, 66, 105, 116, 115, 120, 112, -71, 99, 82, -108, -118, 46, 73, 44, -10, -31, 34, -25, 89, -64, 55, -25, 116, 0, 20, 104, 116, 116, 112, 58, 47, 47, 119, 119, 119, 46, 103, 111, 111, 103, 108, 101, 46, 102, 114 };
	private static final byte[] BYTECMS_MOD_0 = new byte[] { -84, -19, 0, 5, 115, 114, 0, 55, 99, 111, 109, 46, 107, 108, 101, 101, 103, 114, 111, 117, 112, 46, 97, 109, 97, 122, 105, 110, 103, 99, 97, 114, 100, 115, 46, 107, 101, 114, 110, 101, 108, 46, 99, 97, 114, 100, 46, 67, 97, 114, 100, 83, 101, 114, 105, 97, 108, 105, 122, 97, 98, 108, 101, -3, -79, -30, 45, 27, -39, -83, -35, 2, 0, 6, 76, 0, 11, 100, 101, 115, 99, 114, 105, 112, 116, 105, 111, 110, 116, 0, 18, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 76, 0, 4, 110, 97, 109, 101, 113, 0, 126, 0, 1, 76, 0, 10, 111, 116, 104, 101, 114, 68, 97, 116, 97, 115, 116, 0, 19, 76, 106, 97, 118, 97, 47, 117, 116, 105, 108, 47, 72, 97, 115, 104, 77, 97, 112, 59, 76, 0, 4, 116, 121, 112, 101, 116, 0, 49, 76, 99, 111, 109, 47, 107, 108, 101, 101, 103, 114, 111, 117, 112, 47, 97, 109, 97, 122, 105, 110, 103, 99, 97, 114, 100, 115, 47, 107, 101, 114, 110, 101, 108, 47, 99, 97, 114, 100, 47, 67, 97,
			114, 100, 84, 121, 112, 101, 59, 76, 0, 3, 117, 114, 105, 116, 0, 44, 76, 99, 111, 109, 47, 107, 108, 101, 101, 103, 114, 111, 117, 112, 47, 97, 109, 97, 122, 105, 110, 103, 99, 97, 114, 100, 115, 47, 107, 101, 114, 110, 101, 108, 47, 99, 97, 114, 100, 47, 85, 82, 73, 59, 76, 0, 3, 117, 114, 108, 113, 0, 126, 0, 1, 120, 112, 116, 0, 18, 85, 110, 32, 105, 110, 99, 111, 110, 110, 117, 32, 109, 97, 114, 114, 97, 110, 116, 116, 0, 12, 83, 105, 109, 111, 110, 32, 68, 85, 80, 79, 78, 84, 115, 114, 0, 17, 106, 97, 118, 97, 46, 117, 116, 105, 108, 46, 72, 97, 115, 104, 77, 97, 112, 5, 7, -38, -63, -61, 22, 96, -47, 3, 0, 2, 70, 0, 10, 108, 111, 97, 100, 70, 97, 99, 116, 111, 114, 73, 0, 9, 116, 104, 114, 101, 115, 104, 111, 108, 100, 120, 112, 63, 64, 0, 0, 0, 0, 0, 12, 119, 8, 0, 0, 0, 16, 0, 0, 0, 8, 116, 0, 9, 102, 97, 120, 78, 117, 109, 98, 101, 114, 116, 0, 10, 48, 49, 50, 49, 53, 53, 54, 53, 56, 53, 116, 0, 11, 112, 104, 111, 110, 101, 78, 117, 109, 98, 101, 114, 116, 0,
			10, 48, 54, 50, 53, 53, 49, 53, 54, 51, 50, 116, 0, 5, 112, 84, 121, 112, 101, 126, 114, 0, 74, 99, 111, 109, 46, 107, 108, 101, 101, 103, 114, 111, 117, 112, 46, 97, 109, 97, 122, 105, 110, 103, 99, 97, 114, 100, 115, 46, 112, 108, 117, 103, 105, 110, 115, 46, 115, 97, 109, 112, 108, 101, 112, 108, 117, 103, 105, 110, 46, 99, 97, 114, 100, 46, 67, 97, 114, 100, 80, 101, 111, 112, 108, 101, 36, 80, 101, 114, 115, 111, 110, 84, 121, 112, 101, 0, 0, 0, 0, 0, 0, 0, 0, 18, 0, 0, 120, 114, 0, 14, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 69, 110, 117, 109, 0, 0, 0, 0, 0, 0, 0, 0, 18, 0, 0, 120, 112, 116, 0, 9, 65, 78, 79, 78, 89, 77, 79, 85, 83, 116, 0, 8, 112, 83, 117, 114, 110, 97, 109, 101, 116, 0, 6, 68, 85, 80, 79, 78, 84, 116, 0, 5, 112, 78, 97, 109, 101, 116, 0, 5, 83, 105, 109, 111, 110, 116, 0, 5, 101, 109, 97, 105, 108, 116, 0, 12, 106, 112, 64, 101, 109, 97, 105, 108, 46, 99, 111, 109, 116, 0, 12, 112, 68, 101, 115, 99, 114, 105, 112, 116, 105, 111, 110, 116, 0,
			60, 85, 110, 101, 32, 112, 101, 114, 115, 111, 110, 110, 101, 32, 113, 117, 101, 32, 106, 101, 32, 110, 101, 32, 99, 111, 110, 110, 97, 105, 115, 32, 112, 97, 115, 32, 109, 97, 105, 115, 32, 113, 117, 105, 32, -61, -96, 32, 108, 39, 97, 105, 114, 32, 115, 121, 109, 112, 97, 32, 33, 116, 0, 3, 97, 103, 101, 115, 114, 0, 17, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 73, 110, 116, 101, 103, 101, 114, 18, -30, -96, -92, -9, -127, -121, 56, 2, 0, 1, 73, 0, 5, 118, 97, 108, 117, 101, 120, 114, 0, 16, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 78, 117, 109, 98, 101, 114, -122, -84, -107, 29, 11, -108, -32, -117, 2, 0, 0, 120, 112, 0, 0, 0, 65, 120, 126, 114, 0, 62, 99, 111, 109, 46, 107, 108, 101, 101, 103, 114, 111, 117, 112, 46, 97, 109, 97, 122, 105, 110, 103, 99, 97, 114, 100, 115, 46, 112, 108, 117, 103, 105, 110, 115, 46, 115, 97, 109, 112, 108, 101, 112, 108, 117, 103, 105, 110, 46, 99, 97, 114, 100, 46, 67, 97, 114, 100, 84, 121, 112, 101, 115, 0, 0, 0, 0, 0, 0, 0,
			0, 18, 0, 0, 120, 113, 0, 126, 0, 16, 116, 0, 6, 80, 69, 79, 80, 76, 69, 115, 114, 0, 42, 99, 111, 109, 46, 107, 108, 101, 101, 103, 114, 111, 117, 112, 46, 97, 109, 97, 122, 105, 110, 103, 99, 97, 114, 100, 115, 46, 107, 101, 114, 110, 101, 108, 46, 99, 97, 114, 100, 46, 85, 82, 73, -86, -19, -117, 77, 78, 119, 45, 38, 2, 0, 1, 76, 0, 4, 117, 117, 105, 100, 116, 0, 16, 76, 106, 97, 118, 97, 47, 117, 116, 105, 108, 47, 85, 85, 73, 68, 59, 120, 112, 115, 114, 0, 14, 106, 97, 118, 97, 46, 117, 116, 105, 108, 46, 85, 85, 73, 68, -68, -103, 3, -9, -104, 109, -123, 47, 2, 0, 2, 74, 0, 12, 108, 101, 97, 115, 116, 83, 105, 103, 66, 105, 116, 115, 74, 0, 11, 109, 111, 115, 116, 83, 105, 103, 66, 105, 116, 115, 120, 112, -115, -69, -71, -62, -37, -82, -120, -13, -87, 106, -113, 58, -69, 122, 52, 90, 116, 0, 20, 104, 116, 116, 112, 58, 47, 47, 119, 119, 119, 46, 103, 111, 111, 103, 108, 101, 46, 102, 114 };
	//created by https://cryptii.com/base64-to-hex
	private static final byte[] BYTE_MOD_3 = { 0x69, (byte) 0xb7, 0x1d, 0x79, (byte) 0xf8, 0x21, (byte) 0x8a, 0x39, 0x25, (byte) 0x9a, 0x7a, 0x29, (byte) 0xaa, (byte) 0xbb, 0x2d, (byte) 0xba, (byte) 0xfc, 0x31, (byte) 0xcb, 0x3f,
			(byte) 0xf4, (byte) 0xd7, 0x6d, (byte) 0xf8, (byte) 0xe7, (byte) 0xae, (byte) 0xfc, (byte) 0xf7, (byte) 0xe0, 0x01, 0x08, 0x31, 0x05, 0x18, 0x72, 0x09, 0x28, (byte) 0xb3, 0x0d, 0x38,
			(byte) 0xf4, 0x11, 0x49, 0x35, 0x15, 0x59, 0x76, 0x19 };

	/**
	 * Expression régulière vérifiée par les base 64.
	 */
	private static final Pattern REGEX = Pattern.compile("[a-zA-Z0-9_-]*[=]*");

	/** {@inheritDoc} */
	@Override
	public Codec<byte[], String> obtainCodec(final CodecManager codecManager) {
		return codecManager.getBase64Codec();
	}

	/** {@inheritDoc} */
	@Override
	@Test
	public void testNull() {
		assertNull(codec.encode(null));
		assertNull(codec.decode(null));
	}

	/** {@inheritDoc} */
	@Override
	@Test
	public void testEncode() {
		checkEncode("Aladdin:open sesame".getBytes(), "QWxhZGRpbjpvcGVuIHNlc2FtZQ=="); // example from RFC 2617
		checkEncode("".getBytes(), "");
		checkEncode("1".getBytes(), "MQ==");
		checkEncode("22".getBytes(), "MjI=");
		checkEncode("333".getBytes(), "MzMz");
		checkEncode("4444".getBytes(), "NDQ0NA==");
		checkEncode("55555".getBytes(), "NTU1NTU=");
		checkEncode("abc:def".getBytes(), "YWJjOmRlZg==");
		checkEncode("Lorem?ipsum".getBytes(), "TG9yZW0_aXBzdW0=");
		checkEncode("Lorem>ipsum".getBytes(), "TG9yZW0-aXBzdW0=");

		final String encodedValue = codec.encode(TEXT.getBytes());
		assertEquals(TEXT64, encodedValue);
		checkEncode(BYTE_MOD_3, "abcdefghijklmnopqrstuvwxyz_0123456789-ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	}

	/** {@inheritDoc} */
	@Override
	@Test
	public void testDecode() {
		assertEncodeDecodeEquals(TEXT.getBytes());
		assertEncodeDecodeEquals(BYTECMS_MOD_0, "");
		assertEncodeDecodeEquals(BYTECMS_MOD_1, "=");
		assertEncodeDecodeEquals(BYTECMS_MOD_2, "==");
		assertEncodeDecodeEquals(BYTE_MOD_3, "");
	}

	@Test
	public void testBase64UrlEncode() {
		assertEncodeDecodeUrlReplace(TEXT.getBytes());
		assertEncodeDecodeUrlReplace(BYTECMS_MOD_0);
		assertEncodeDecodeUrlReplace(BYTECMS_MOD_1);
		assertEncodeDecodeUrlReplace(BYTECMS_MOD_2);
		assertEncodeDecodeUrlReplace(BYTE_MOD_3);
	}

	private void assertEncodeDecodeUrlReplace(final byte[] bytes) {
		assertEncodeDecodeEquals(bytes);
		final String urlEncodedValue = codec.encode(bytes);
		final String stdEncodedValue = Base64.getEncoder().encodeToString(bytes);
		assertEquals(stdEncodedValue, urlEncodedValue.replace('-', '+').replace('_', '/'));
	}

	private void assertEncodeDecodeEquals(final byte[] original, final String endWith) {
		assertEncodeDecodeEquals(original);
		final String encodedValue = codec.encode(original);
		assertTrue(encodedValue.endsWith(endWith));
	}

	private void assertEncodeDecodeEquals(final byte[] original) {
		final String encodedValue = codec.encode(original);
		assertEquals(new String(original), new String(codec.decode(encodedValue)));
	}

	/** {@inheritDoc} */
	@Override
	@Test
	public void testFailDecode() {
		//rien
	}

	/** {@inheritDoc} */
	@Override
	protected void checkEncodedValue(final String encodedValue) {
		//On vérifie que la valeur encodée respecte bien le pattern.
		assertTrue(REGEX.matcher(encodedValue).matches(), "Pattern Base 64");
	}

}
