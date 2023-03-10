package com.enicom.nims.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

public class StringUtil extends StringUtils {

	public static String ltrim(String str) {
		int len = str.length();
		int idx = 0;

		while ((idx < len) && (str.charAt(idx) <= ' ')) {
			idx++;
		}
		return str.substring(idx, len);
	}

	public static String rtrim(String str) {
		int len = str.length();

		while ((0 < len) && (str.charAt(len - 1) <= ' ')) {
			len--;
		}
		return str.substring(0, len);
	}

	public static String getDigit(String sValue) {
		if (sValue == null || "".equals(sValue)) {
			return "";
		}

		String digitStr = "0123456789";
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < sValue.length(); i++) {

			char cValue = sValue.charAt(i);
			for (int j = 0; j < digitStr.length(); j++) {

				char digit = digitStr.charAt(j);
				if (cValue == digit) {
					sb.append(cValue);
				}
			}
		}
		return sb.toString();
	}

	public static String changeMoney(String str) {
		DecimalFormat df = new DecimalFormat("###,###");

		return df.format(parseInt(str));
	}

	public static String removeComma(String str) {
		String rtnValue = str;
		if (isNull(str)) {
			return "";
		}

		rtnValue = replace(rtnValue, ",", "");
		return rtnValue;
	}

	public static boolean isNull(String str) {
		return (str == null || (str.trim().length()) == 0);
	}

	public static boolean isNull(Object obj) {
		String str = null;
		if (obj instanceof String) {
			str = (String) obj;
		} else {
			return true;
		}

		return isNull(str);
	}

	public static boolean isNotNull(String str) {
		if (isNull(str)) {
			return false;

		} else {
			return true;
		}
	}

	public static boolean isNotNull(Object obj) {
		String str = null;
		if (obj instanceof String) {
			str = (String) obj;
		} else {
			return false;
		}

		return isNotNull(str);
	}

	public static String nvl(String value) {
		return nvl(value, "");
	}

	public static String nvl(Object value) {
		Object rtnValue = value;
		if (rtnValue == null || !"java.lang.String".equals(rtnValue.getClass().getName())) {
			rtnValue = "";
		}

		return nvl((String) rtnValue, "");
	}

	public static String nvl(String value, String defaultValue) {
		if (isNull(value)) {
			return defaultValue;
		}

		return value.trim();
	}

	public static String nvl(Object value, String defaultValue) {
		String valueStr = nvl(value);
		if (isNull(valueStr)) {
			return defaultValue;
		}

		return valueStr.trim();
	}

	public static String ksc2asc(String str) {
		String result = "";

		if (isNull(str)) {
			result = "";
		} else {
			try {
				result = new String(str.getBytes("euc-kr"), "8859_1");
			} catch (Exception e) {
				result = "";
			}
		}

		return result;
	}

	public static String asc2ksc(String str) {
		String result = "";

		if (isNull(str)) {
			result = "";
		} else {
			try {
				result = new String(str.getBytes("8859_1"), "euc-kr");
			} catch (Exception e) {
				result = "";
			}
		}

		return result;
	}

	public static String hexHex2Str(String strString) {
		String ori = "";

		try {
			byte[] result = strString.getBytes("Cp933");
			String temp = "";

			for (int i = 1; i < result.length - 1; i++) {
				temp = Integer.toHexString(result[i]).toUpperCase();
				ori = ori + temp.substring(temp.length() - 2);
			}
		} catch (Exception _ex) {
			_ex.printStackTrace();
		}
		return ori;
	}

	public static String hexStr2Hex(String hexaString) {
		int byteLength = hexaString.length() / 2;

		byte[] result = new byte[byteLength];

		for (int i = 0; i < byteLength; i++) {

			String frag = hexaString.substring(i * 2, i * 2 + 2);
			result[i] = (byte) Integer.parseInt(frag, 16);

		}

		try {
			byte[] temp4 = new byte[result.length + 2];
			System.arraycopy(result, 0, temp4, 1, result.length);

			temp4[0] = 0x0E;
			temp4[temp4.length - 1] = 0x0F;

			String ori = new String(temp4, "Cp933");
			return getNcharToString(ori).trim();

		} catch (Exception _ex) {
			_ex.printStackTrace();
		}
		return null;
	}

	public static String getNcharToString(String Nchar) {

		if (Nchar == null || Nchar.length() == 0)
			Nchar = "";
		else
			Nchar = Nchar.replace("  ", " ");

		return Nchar;

	}

	public static String hexStr2Str(String hexaString) {
		int byteLength = hexaString.length() / 2;

		byte[] result = new byte[byteLength];

		for (int i = 0; i < byteLength; i++) {

			String frag = hexaString.substring(i * 2, i * 2 + 2);
			result[i] = (byte) Integer.parseInt(frag, 16);

		}

		try {
			byte[] temp4 = new byte[result.length + 2];
			System.arraycopy(result, 0, temp4, 1, result.length);

			temp4[0] = 0x0E;
			temp4[temp4.length - 1] = 0x0F;

			String ori = new String(temp4, "Cp933");

			return ori.trim();

		} catch (Exception _ex) {
			_ex.printStackTrace();
		}
		return null;
	}

	public static int parseInt(String value) {
		return parseInt(value, 0);
	}

	public static int parseInt(Object value) {
		String valueStr = nvl(value);
		return parseInt(valueStr, 0);
	}

	public static int parseInt(Object value, int defaultValue) {
		String valueStr = nvl(value);
		return parseInt(valueStr, defaultValue);
	}

	public static int parseInt(String value, int defaultValue) {
		int returnValue = 0;

		if (isNull(value)) {
			returnValue = defaultValue;
		} else if (!isNumeric(value)) {
			returnValue = defaultValue;
		} else {
			returnValue = Integer.parseInt(value);
		}

		return returnValue;
	}

	public static long parseLong(String value) {
		return parseLong(value, 0);
	}

	public static long parseLong(String value, long defaultValue) {
		long returnValue = 0;

		if (isNull(value)) {
			returnValue = defaultValue;
		} else if (!isNumeric(value)) {
			returnValue = defaultValue;
		} else {
			returnValue = Long.parseLong(value);
		}

		return returnValue;
	}

	public static String stackTraceToString(Throwable e) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return "------\r\n" + sw.toString() + "------\r\n";
		} catch (Exception e2) {
			return StringUtil.stackTraceToString2(e);
		}
	}

	public static String stackTraceToString2(Throwable e) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		PrintStream p = new PrintStream(b);
		e.printStackTrace(p);
		p.close();
		String stackTrace = b.toString();
		try {
			b.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return stackTrace;
	}

	public static String convertHtmlBr(String comment) {
		String rtnValue = "";
		if (isNull(comment)) {
			return "";
		}

		rtnValue = replace(comment, "\r\n", "<br>");

		return rtnValue;
	}

	public static List<String> changeList(String[] values) {
		List<String> list = new ArrayList<String>();

		if (values == null) {
			return list;
		}
		for (int i = 0, n = values.length; i < n; i++) {
			list.add(values[i]);
		}

		return list;
	}

	public static String[] toTokenArray(String str, String sep) {

		String[] temp = null;

		try {
			StringTokenizer st = new StringTokenizer(str, sep);
			temp = new String[st.countTokens()];
			int index = 0;
			while (st.hasMoreTokens()) {
				temp[index++] = st.nextToken();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return temp;
	}

	static public Hashtable<String, String> get_tokens(String p_str, String p_delim) throws Exception {
		Hashtable<String, String> v_ht = new Hashtable<String, String>();

		StringTokenizer v_tokenizer = new StringTokenizer(p_str, p_delim);

		int v_cnt = 1;

		try {
			while (v_tokenizer.hasMoreTokens())
				v_ht.put(String.valueOf(v_cnt++), v_tokenizer.nextToken().trim());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}

		return v_ht;
	}

	public static String strip(String str, String str1) {

		if (str == null || "".equals(str.trim()))
			return "";

		String temp = str;
		int pos = -1;
		while ((pos = temp.indexOf(str1, pos)) != -1) {
			String left = temp.substring(0, pos);
			String right = temp.substring(pos + 1, temp.length());
			temp = left + "" + right;
			pos += 1;
		}
		return temp;
	}

	public static String ksc2utf8(String str) {
		String result = "";

		if (isNull(str)) {
			result = "";
		} else {
			try {
				result = new String(str.getBytes("euc-kr"), "utf-8");
			} catch (Exception e) {
				result = "";
			}
		}

		return result;
	}

	public static String utf82ksc(String str) {
		String result = "";

		if (isNull(str)) {
			result = "";
		} else {
			try {
				result = new String(str.getBytes("utf-8"), "euc-kr");
			} catch (Exception e) {
				result = "";
			}
		}

		return result;
	}

	public static String convertInconding(String str, String t1, String t2) {
		String result = "";

		if (isNull(str)) {
			result = "";
		} else {
			try {
				result = new String(str.getBytes(t1), t2);
			} catch (Exception e) {
				result = "";
			}
		}

		return result;
	}

	public static String changeQuotation(String str) {
		String rtnValue = str;
		rtnValue = nvl(rtnValue);
		rtnValue = replace(replace(replace(rtnValue, "'", "&#39;"), "\"", "&#34;"), "\r\n", "<br>");

		return rtnValue;
	}

	public static String changeQuotation(Object obj) {
		if (isStringInteger(obj)) {
			return changeQuotation(String.valueOf(obj));
		}

		return "";
	}

	public static boolean isStringInteger(Object obj) {

		boolean flag = false;

		if (obj instanceof String || obj instanceof Integer) {
			flag = true;
		}

		return flag;
	}

	public static boolean isNumber(String number) {
		boolean flag = true;
		if (number == null || "".equals(number))
			return false;

		int size = number.length();
		int st_no = 0;

		if (number.charAt(0) == 45)
			st_no = 1;

		for (int i = st_no; i < size; ++i) {
			if (!(48 <= ((int) number.charAt(i)) && 57 >= ((int) number.charAt(i)))) {
				flag = false;
				break;
			}

		}
		return flag;
	}

	public static String percentValue(int value, int total) {
		double val = Double.parseDouble(String.valueOf(value)) / Double.parseDouble(String.valueOf(total)) * 100;

		DecimalFormat df = new DecimalFormat("##0.0");
		return df.format(val);
	}

	public static String replaceXSS(String sourceString) {
		String rtnValue = null;
		if (sourceString != null) {
			rtnValue = sourceString;
			if (rtnValue.indexOf("<x-") == -1) {
				rtnValue = rtnValue.replaceAll("< *(j|J)(a|A)(v|V)(a|A)(s|S)(c|C)(r|R)(i|I)(p|P)(t|T)",
						"<x-javascript");
				rtnValue = rtnValue.replaceAll("< *(v|V)(b|B)(s|S)(c|C)(r|R)(i|I)(p|P)(t|T)", "<x-vbscript");
				rtnValue = rtnValue.replaceAll("< *(s|S)(c|C)(r|R)(i|I)(p|P)(t|T)", "<x-script");
				rtnValue = rtnValue.replaceAll("< *(i|I)(f|F)(r|R)(a|A)(m|M)(e|E)", "<x-iframe");
				rtnValue = rtnValue.replaceAll("< *(f|F)(r|R)(a|A)(m|M)(e|E)", "<x-frame");
				rtnValue = rtnValue.replaceAll("(e|E)(x|X)(p|P)(r|R)(e|E)(s|S)(s|S)(i|I)(o|O)(n|N)", "x-expression");
				rtnValue = rtnValue.replaceAll("(a|A)(l|L)(e|E)(r|R)(t|T)", "x-alert");
				rtnValue = rtnValue.replaceAll(".(o|O)(p|P)(e|E)(n|N)", ".x-open");
				rtnValue = rtnValue.replaceAll("< *(m|M)(a|A)(r|R)(q|Q)(u|U)(e|E)(e|E)", "<x-marquee");
				rtnValue = rtnValue.replaceAll("&#", "&amp;#");
			}
		}

		return rtnValue;
	}

	public static String translate(String str) {
		if (str == null)
			return null;

		StringBuffer buf = new StringBuffer();
		char[] c = str.toCharArray();
		int len = c.length;

		for (int i = 0; i < len; i++) {
			if (c[i] == '&')
				buf.append("&amp;");
			else if (c[i] == '<')
				buf.append("&lt;");
			else if (c[i] == '>')
				buf.append("&gt;");
			else if (c[i] == '"')
				buf.append("&quot;"); // (char)34
			else if (c[i] == '\'')
				buf.append("&#039;"); // (char)39
			else
				buf.append(c[i]);
		}
		return buf.toString();
	}

	public static String pad(String src, String pad, int totLen, int mode) {
		String paddedString = "";

		if (src == null)
			return "";
		int srcLen = src.length();

		if ((totLen < 1) || (srcLen >= totLen))
			return src;

		for (int i = 0; i < (totLen - srcLen); i++) {
			paddedString += pad;
		}

		if (mode == -1)
			paddedString += src; // front padding
		else
			paddedString = src + paddedString; // back padding

		return paddedString;
	}

	public static String lpad(String strSource, int iLength, char cPadder) {
		StringBuffer sbBuffer = null;

		if (!isEmpty(strSource)) {
			int iByteSize = getByteSize(strSource);
			if (iByteSize > iLength) {
				return strSource.substring(0, iLength);
			} else if (iByteSize == iLength) {
				return strSource;
			} else {
				int iPadLength = iLength - iByteSize;
				sbBuffer = new StringBuffer();
				for (int j = 0; j < iPadLength; j++) {
					sbBuffer.append(cPadder);
				}
				sbBuffer.append(strSource);
				return sbBuffer.toString();
			}
		}

		// int iPadLength = iLength;
		sbBuffer = new StringBuffer();
		for (int j = 0; j < iLength; j++) {
			sbBuffer.append(cPadder);
		}
		return sbBuffer.toString();
	}

	public static String rpad(String strSource, int iLength, char cPadder) {
		StringBuffer sbBuffer = null;
		if (!isEmpty(strSource)) {
			int iByteSize = getByteSize(strSource);
			if (iByteSize > iLength) {
				return strSource.substring(0, iLength);
			} else if (iByteSize == iLength) {
				return strSource;
			} else {
				int iPadLength = iLength - iByteSize;
				sbBuffer = new StringBuffer(strSource);
				for (int j = 0; j < iPadLength; j++) {
					sbBuffer.append(cPadder);
				}
				return sbBuffer.toString();
			}
		}
		sbBuffer = new StringBuffer();
		for (int j = 0; j < iLength; j++) {
			sbBuffer.append(cPadder);
		}
		return sbBuffer.toString();
	}

	public static int getByteSize(String str) {
		if (str == null || str.length() == 0)
			return 0;
		byte[] byteArray = null;
		try {
			byteArray = str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException ex) {
		}
		if (byteArray == null)
			return 0;
		return byteArray.length;
	}

	public static String strCut(String srcString, int nLength, boolean isNoTag, boolean isAddDot) {
		String rtnVal = srcString;
		int oF = 0, oL = 0, rF = 0, rL = 0;
		int nLengthPrev = 0;


		if (isNoTag) {
			Pattern p = Pattern.compile("<(/?)([^<>]*)?>", Pattern.CASE_INSENSITIVE);
			rtnVal = p.matcher(rtnVal).replaceAll("");
		}
		rtnVal = rtnVal.replaceAll("&amp;", "&");
		rtnVal = rtnVal.replaceAll("(!/|\r|\n|&nbsp;)", "");
		try {
			byte[] bytes = rtnVal.getBytes("UTF-8");

			int j = 0;
			if (nLengthPrev > 0)
				while (j < bytes.length) {
					if ((bytes[j] & 0x80) != 0) {
						oF += 2;
						rF += 3;
						if (oF + 2 > nLengthPrev) {
							break;
						}
						j += 3;
					} else {
						if (oF + 1 > nLengthPrev) {
							break;
						}
						++oF;
						++rF;
						++j;
					}
				}

			j = rF;

			while (j < bytes.length) {
				if ((bytes[j] & 0x80) != 0) {
					if (oL + 2 > nLength) {
						break;
					}
					oL += 2;
					rL += 3;
					j += 3;
				} else {
					if (oL + 1 > nLength) {
						break;
					}
					++oL;
					++rL;
					++j;
				}
			}

			rtnVal = new String(bytes, rF, rL, "UTF-8");

			if (isAddDot && rF + rL + 3 <= bytes.length) {
				rtnVal += "...";
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return srcString;
		}

		return rtnVal;
	}

	static public String get_string_from(String p_str, int p_s, int p_e) {
		try {
			return (p_str.length() > p_e) ? p_str.substring(p_s, p_e) + "..." : p_str;
		} catch (Exception e) {
			return p_str.length() > p_s ? p_str.substring(p_s, p_str.length()) : "";
		}
	}

	static public String cut_string_from(String p_data, String p_start, String p_end) {
		int v_pos = 0;
		String v_data = p_data;

		try {
			if ((v_pos = v_data.indexOf(p_start)) == -1)
				return v_data;

			String v_head = v_data.substring(0, v_pos);
			v_data = v_data.substring(v_pos, v_data.length());
			return v_head + v_data.substring(v_data.indexOf(p_end) + p_end.length(), v_data.length());
		}

		catch (Exception e) {
			return p_data;
		}
	}

	static public String cvt_string_from(String p_text, String p_org, String p_tar) {
		int v_locate = 0, v_prev = -1;
		String v_tmp = "";

		if (p_text == null || "".equals(p_text))
			return "";

		while (v_locate != -1) {
			if ((v_locate = p_text.indexOf(p_org)) == -1)
				break;

			if (v_locate == v_prev)
				break;

			v_prev = v_locate;

			v_tmp = p_text.substring(0, v_locate);
			v_tmp += p_tar;
			v_tmp += p_text.substring(v_locate + p_org.length(), p_text.length());

			p_text = v_tmp;
		}
		;

		return p_text;
	}

	public static String mReplaceBRTag(String inStr) {
		int length = inStr.length();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			String tmp = inStr.substring(i, i + 1);
			if ("\n".compareTo(tmp) == 0)
				buffer.append("<br>");
			if (" ".compareTo(tmp) == 0)
				buffer.append("&nbsp;");
			else
				buffer.append(tmp);
		}

		return buffer.toString();
	}

	public static String convertSQLINString(String[] value) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < value.length; i++) {
			sb.append(value[i]);
			if (i < value.length - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	public static Timestamp getCurrentTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Calendar cal = Calendar.getInstance();
		String today = formatter.format(cal.getTime());
		Timestamp ts = Timestamp.valueOf(today);

		return ts;
	}

	public static String unscript(String data) {
		if (data == null || data.trim().equals("")) {
			return "";
		}

		String ret = data;

		ret = ret.replaceAll("<(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;script");
		ret = ret.replaceAll("</(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;/script");

		ret = ret.replaceAll("<(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;object");
		ret = ret.replaceAll("</(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;/object");

		ret = ret.replaceAll("<(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;applet");
		ret = ret.replaceAll("</(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;/applet");

		ret = ret.replaceAll("<(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");
		ret = ret.replaceAll("</(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");

		ret = ret.replaceAll("<(F|f)(O|o)(R|r)(M|m)", "&lt;form");
		ret = ret.replaceAll("</(F|f)(O|o)(R|r)(M|m)", "&lt;form");

		return ret;
	}

	public static String replaceStr(String data) {
		String ret = data;

		ret = ret.replace("\r\n", "<br>");
		ret = ret.replace("<", "&lt;");
		ret = ret.replace(">", "&gt;");
		ret = ret.replace("&", "&amp;");
		ret = ret.replace("'", "&apos;");
		ret = ret.replace("\"", "&quot;");

		return ret;
	}
	public static String getParamSqlInj(String text) {
		return StringEscapeUtils.unescapeHtml4(text);
	}

	public static String getParamSearchWord(String text) {
		text = text.toUpperCase();
		if (text.indexOf(' ') > -1)
			text = text.replaceAll(" ", "%");
		return text;
	}
}