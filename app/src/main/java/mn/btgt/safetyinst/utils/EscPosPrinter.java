package mn.btgt.safetyinst.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import mn.btgt.safetyinst.R;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */

public class EscPosPrinter {
    // Constants
    public static final byte[] CTL_LF = new byte[]{0x0a}; // Print and line feed
    public static final byte[] CTL_FF = new byte[]{0x0c}; // Form feed
    public static final byte[] CTL_CR = new byte[]{0x0d}; // Carriage return
    public static final byte[] CTL_HT = new byte[]{0x09}; // Horizontal tab
    public static final byte[] CTL_SET_HT = new byte[]{0x1b, 0x44}; // Set horizontal tab positions
    public static final byte[] CTL_VT = new byte[]{0x1b, 0x64, 0x04}; // Vertical tab
    // Printer hardware
    public static final byte[] HW_INIT = new byte[]{0x1b, 0x40}; // Clear data in buffer and reset modes
    public static final byte[] HW_SELECT = new byte[]{0x1b, 0x3d, 0x01}; // Printer select
    public static final byte[] HW_RESET = new byte[]{0x1b, 0x3f, 0x0a, 0x00}; // Reset printer hardware
    // Cash Drawer
    public static final byte[] CD_KICK_2 = new byte[]{0x1b, 0x70, 0x00}; // Sends a pulse to pin 2 [],
    public static final byte[] CD_KICK_5 = new byte[]{0x1b, 0x70, 0x01}; // Sends a pulse to pin 5 [],
    // Paper
    public static final byte[] PAPER_FULL_CUT = new byte[]{0x1d, 0x56, 0x00}; // Full cut paper
    public static final byte[] PAPER_PART_CUT = new byte[]{0x1d, 0x56, 0x01}; // Partial cut paper
    // Text format
    public static final byte[] TXT_NORMAL = new byte[]{0x1b, 0x21, 0x00}; // Normal text
    public static final byte[] TXT_2HEIGHT = new byte[]{0x1b, 0x21, 0x10}; // Double height text
    public static final byte[] TXT_2WIDTH = new byte[]{0x1b, 0x21, 0x20}; // Double width text
    public static final byte[] TXT_4SQUARE = new byte[]{0x1b, 0x21, 0x30}; // Quad area text
    public static final byte[] TXT_UNDERL_OFF = new byte[]{0x1b, 0x2d, 0x00}; // Underline font OFF
    public static final byte[] TXT_UNDERL_ON = new byte[]{0x1b, 0x2d, 0x01}; // Underline font 1-dot ON
    public static final byte[] TXT_UNDERL2_ON = new byte[]{0x1b, 0x2d, 0x02}; // Underline font 2-dot ON
    public static final byte[] TXT_BOLD_OFF = new byte[]{0x1b, 0x45, 0x00}; // Bold font OFF
    public static final byte[] TXT_BOLD_ON = new byte[]{0x1b, 0x45, 0x01}; // Bold font ON
    public static final byte[] TXT_ITALIC_ON = new byte[]{0x1b, 0x21, 0x40}; // Italic font ON
    public static final byte[] TXT_ITALIC_OFF = new byte[]{0x1b, 0x21, 0x00}; // Italic font OFF
    public static final byte[] TXT_FONT_A = new byte[]{0x1b, 0x4d, 0x00}; // Font type A
    public static final byte[] TXT_FONT_B = new byte[]{0x1b, 0x4d, 0x01}; // Font type B
    public static final byte[] TXT_ALIGN_LT = new byte[]{0x1b, 0x61, 0x00}; // Left justification
    public static final byte[] TXT_ALIGN_CT = new byte[]{0x1b, 0x61, 0x01}; // Centering
    public static final byte[] TXT_ALIGN_RT = new byte[]{0x1b, 0x61, 0x02}; // Right justification
    // Char code table
    public static final byte[] CHARCODE_WIN1251 = new byte[]{0x1b, 0x74, 0x11}; // Western European Windows Code Set
    // Barcode format
    public static final byte[] BARCODE_TXT_OFF = new byte[]{0x1d, 0x48, 0x00}; // HRI barcode chars OFF
    public static final byte[] BARCODE_TXT_ABV = new byte[]{0x1d, 0x48, 0x01}; // HRI barcode chars above
    public static final byte[] BARCODE_TXT_BLW = new byte[]{0x1d, 0x48, 0x02}; // HRI barcode chars below
    public static final byte[] BARCODE_TXT_BTH = new byte[]{0x1d, 0x48, 0x03}; // HRI barcode chars both above and below
    public static final byte[] BARCODE_FONT_A = new byte[]{0x1d, 0x66, 0x00}; // Font type A for HRI barcode chars
    public static final byte[] BARCODE_FONT_B = new byte[]{0x1d, 0x66, 0x01}; // Font type B for HRI barcode chars
    public static final byte[] BARCODE_HEIGHT = new byte[]{0x1d, 0x68, 0x50}; // Barcode Height [1-255],
    public static final byte[] BARCODE_WIDTH = new byte[]{0x1d, 0x77, 0x02}; // Barcode Width  [2-6],
    public static final byte[] BARCODE_UPC_A = new byte[]{0x1d, 0x6b, 0x00}; // Barcode type UPC-A
    public static final byte[] BARCODE_UPC_E = new byte[]{0x1d, 0x6b, 0x01}; // Barcode type UPC-E
    public static final byte[] BARCODE_EAN13 = new byte[]{0x1d, 0x6b, 0x02}; // Barcode type EAN13
    public static final byte[] BARCODE_EAN8 = new byte[]{0x1d, 0x6b, 0x03}; // Barcode type EAN8
    public static final byte[] BARCODE_CODE39 = new byte[]{0x1d, 0x6b, 0x04}; // Barcode type CODE39
    public static final byte[] BARCODE_ITF = new byte[]{0x1d, 0x6b, 0x05}; // Barcode type ITF
    public static final byte[] BARCODE_NW7 = new byte[]{0x1d, 0x6b, 0x06}; // Barcode type NW7
    // Image format
    public static final byte[] S_RASTER_N = new byte[]{0x1d, 0x76, 0x30, 0x00}; // Set raster image normal size
    public static final byte[] S_RASTER_2W = new byte[]{0x1d, 0x76, 0x30, 0x01}; // Set raster image double width
    public static final byte[] S_RASTER_2H = new byte[]{0x1d, 0x76, 0x30, 0x02}; // Set raster image double height
    public static final byte[] S_RASTER_Q = new byte[]{0x1d, 0x76, 0x30, 0x03}; // Set raster image quadruple
    // Printing Density
    public static final byte[] PD_N50 = new byte[]{0x1d, 0x7c, 0x00}; // Printing Density -50%
    public static final byte[] PD_N37 = new byte[]{0x1d, 0x7c, 0x01}; // Printing Density -37.5%
    public static final byte[] PD_N25 = new byte[]{0x1d, 0x7c, 0x02}; // Printing Density -25%
    public static final byte[] PD_N12 = new byte[]{0x1d, 0x7c, 0x03}; // Printing Density -12.5%
    public static final byte[] PD_0 = new byte[]{0x1d, 0x7c, 0x04}; // Printing Density  0%
    public static final byte[] PD_P50 = new byte[]{0x1d, 0x7c, 0x08}; // Printing Density +50%
    public static final byte[] PD_P37 = new byte[]{0x1d, 0x7c, 0x07}; // Printing Density +37.5%
    public static final byte[] PD_P25 = new byte[]{0x1d, 0x7c, 0x06}; // Printing Density +25%
    public static final byte[] PD_P12 = new byte[]{0x1d, 0x7c, 0x05}; // Printing Density +12.5%
    // CHaracter Mode
    public static final byte[] CM_MULTI_BYTE_OFF = new byte[]{0x1C, 0x2E}; // Cancel Kangi Character Mode
    public static final byte[] CM_MULTI_BYTE_ON = new byte[]{0x1C, 0x26}; // Select Kanji Character Mode

    private static final Hashtable<String, String> MAP_KRILL_TO_LATIN;
    private static final Hashtable<Integer, Integer> UNICODE_TO_ASCII;
    private static final List<String> pattern = new ArrayList<>(Arrays.asList("1", "X", "0"));
    public static final String TAG = "ESCPOSPrinter";
    static {
        Hashtable<String, String> aMap = new Hashtable<String, String>();
        aMap.put("Ё", "YO");
        aMap.put("Й", "I");
        aMap.put("Ц", "С");
        aMap.put("У", "U");
        aMap.put("К", "K");
        aMap.put("Е", "E");
        aMap.put("Н", "N");
        aMap.put("Г", "G");
        aMap.put("Ш", "Sh");
        aMap.put("Щ", "Sch");
        aMap.put("З", "Z");
        aMap.put("Х", "H");
        aMap.put("Ъ", "'");
        aMap.put("ё", "yo");
        aMap.put("й", "i");
        aMap.put("ц", "с");
        aMap.put("у", "u");
        aMap.put("к", "k");
        aMap.put("е", "e");
        aMap.put("н", "n");
        aMap.put("г", "g");
        aMap.put("ш", "sh");
        aMap.put("щ", "sch");
        aMap.put("з", "z");
        aMap.put("х", "h");
        aMap.put("ъ", "'");
        aMap.put("Ф", "F");
        aMap.put("Ы", "I");
        aMap.put("В", "V");
        aMap.put("А", "A");
        aMap.put("П", "P");
        aMap.put("Р", "R");
        aMap.put("О", "O");
        aMap.put("Л", "L");
        aMap.put("Д", "D");
        aMap.put("Ж", "J");
        aMap.put("Э", "E");
        aMap.put("ф", "f");
        aMap.put("ы", "i");
        aMap.put("в", "v");
        aMap.put("а", "a");
        aMap.put("п", "p");
        aMap.put("р", "r");
        aMap.put("о", "o");
        aMap.put("л", "l");
        aMap.put("д", "d");
        aMap.put("ж", "j");
        aMap.put("э", "e");
        aMap.put("Я", "Ya");
        aMap.put("Ч", "Ch");
        aMap.put("С", "S");
        aMap.put("М", "M");
        aMap.put("И", "I");
        aMap.put("Т", "T");
        aMap.put("Ь", "'");
        aMap.put("Б", "B");
        aMap.put("Ю", "Yu");
        aMap.put("я", "ya");
        aMap.put("ч", "ch");
        aMap.put("с", "s");
        aMap.put("м", "m");
        aMap.put("и", "i");
        aMap.put("т", "t");
        aMap.put("ь", "'");
        aMap.put("б", "b");
        aMap.put("ю", "yu");
        aMap.put("Ө", "U");
        aMap.put("ө", "u");
        aMap.put("Ү", "Y");
        aMap.put("ү", "y");
        MAP_KRILL_TO_LATIN = aMap;
    }

    static {
        Hashtable<Integer, Integer> spec_map = new Hashtable<Integer, Integer>();
        spec_map.put(1025, 168); // ТОМ Ё
        spec_map.put(1105, 184); // Жижиш ё

        // Ү үсэгийг Latin V ээр
        spec_map.put(1198, 86); // Том Ү
        spec_map.put(1199, 118); // Жижиг ү

        spec_map.put(1256, 170); // Том Ө
        spec_map.put(1257, 186); // Жижиг ө

        spec_map.put(415, 170); // Том Ө for Lucida sans Unicode
        spec_map.put(629, 186); // Жижиг ө for Lucida sans Unicode
//        // Ү үсэгийг Latin V ээр
//        spec_map.put(1198, (byte)86); // Том Ү
//        spec_map.put(629, (byte)118); // Жижиг ү
        UNICODE_TO_ASCII = spec_map;
    }

    ArrayList<byte[]> line_buffers;
    private String font_converter; // LATIN, ASCII
    private String cut_paper_part; // LATIN, ASCII
    private int total_size; // LATIN, ASCII
    private int codepage = 17; // LATIN, ASCII
    private  boolean print_image = true;
    public EscPosPrinter(){  // test Mode
        this.line_buffers = new ArrayList<byte[]>();
        this.font_converter = "ASCII";
        this.reset();
    }
    public EscPosPrinter(int vendorId, int productId) {
        // TODO Profiles
        this.line_buffers = new ArrayList<byte[]>();
        this.font_converter = "LATIN";
        this.cut_paper_part = "PARTIAL";
        this.reset();
    }
//    public EscPosPrinter(String font_converter, int printer_codepage, Boolean showLogo) {
//        // TODO Profiles
//        Log.d("EscPos Printer ","font : "+font_converter + " codepage : "+printer_codepage+ " showLogo : "+showLogo);
//        this.line_buffers = new ArrayList<byte[]>();
//        this.font_converter = font_converter;
//        this.cut_paper_part = "PARTIAL";
//        this.codepage = printer_codepage;
//        this.print_image = showLogo;
//        this.reset();
//    }
    @Deprecated
    public EscPosPrinter(String font_converter, int printer_codepage) {
        // TODO Profiles
        Log.d("EscPos Printer ","font : "+font_converter + " codepage : "+printer_codepage);
        this.line_buffers = new ArrayList<byte[]>();
        this.font_converter = font_converter;
        this.cut_paper_part = "PARTIAL";
        this.codepage = printer_codepage;
        this.reset();
    }
    public  void  clearData() {
        this.reset();
    }
    private void reset() {
        // this.line_buffers
        this.line_buffers.clear();
        this.total_size = 0;
        this.raw(HW_INIT);
        this.raw(HW_SELECT);
        this.raw(CM_MULTI_BYTE_OFF);
        set_codepage(this.codepage);
    }
    public void set_codepage(int cp){
        this.raw(new byte[]{0x1B, 0x74, (byte) cp});
    }

    public void text(String text) {
        String trans = this.translate(text);
        byte[] convert = this.map_convert(trans);
        this.raw(convert);
    }

    public void raw(byte[] datas) {
//        this.debug_hex(datas, "ADD");
        this.line_buffers.add(datas);
        this.total_size += datas.length;
    }

    public void set_width(int width, int height) {
        // # Width
        if (height == 2 && width == 2) {
            this.raw(TXT_NORMAL);
            this.raw(TXT_4SQUARE);
        } else if (height == 2 && width != 2) {
            this.raw(TXT_NORMAL);
            this.raw(TXT_2HEIGHT);
        } else if (width == 2 && height != 2) {
            this.raw(TXT_NORMAL);
            this.raw(TXT_2WIDTH);
        } else {
            this.raw(TXT_NORMAL);
        }
    }

    public void set_charType(String type) {
        // # Type
        switch (type) {
            case "I":
                this.raw(TXT_ITALIC_ON);
                this.raw(TXT_UNDERL_OFF);
                break;
            case "B":
                this.raw(TXT_BOLD_ON);
                this.raw(TXT_UNDERL_OFF);
                break;
            case "U":
                this.raw(TXT_BOLD_OFF);
                this.raw(TXT_UNDERL_ON);
                break;
            case "U2":
                this.raw(TXT_BOLD_OFF);
                this.raw(TXT_UNDERL2_ON);
                break;
            case "BU":
                this.raw(TXT_BOLD_ON);
                this.raw(TXT_UNDERL_ON);
                break;
            case "BU2":
                this.raw(TXT_BOLD_ON);
                this.raw(TXT_UNDERL2_ON);
                break;
            case "NORMAL":
                this.raw(TXT_BOLD_OFF);
                this.raw(TXT_UNDERL_OFF);
                break;
        }
    }

    public void set_font(String font) {
        // # Font
        if ("B".equals(font)) {
            this.raw(TXT_FONT_B);
        } else {
            this.raw(TXT_FONT_A);
        }
    }

    public void set_align(String align) {
        // # Align
        switch (align) {
            case "CENTER":
                this.raw(TXT_ALIGN_CT);
                break;
            case "RIGHT":
                this.raw(TXT_ALIGN_RT);
                break;
            case "LEFT":
                this.raw(TXT_ALIGN_LT);
                break;
        }
    }

    public void eject_cashdrawer() {
        this.raw(CD_KICK_2);
        this.raw(CD_KICK_5);
    }

    public void cut() {
        if (cut_paper_part == "FULL") {
            this.raw(PAPER_FULL_CUT);
        } else {
            this.raw(PAPER_PART_CUT);
        }
    }

    public void set_density(int density) {
        // # Density
        switch (density) {
            case 0:
                this.raw(PD_N50);
                break;
            case 1:
                this.raw(PD_N37);
                break;
            case 2:
                this.raw(PD_N25);
                break;
            case 3:
                this.raw(PD_N12);
                break;
            case 4:
                this.raw(PD_0);
                break;
            case 5:
                this.raw(PD_P12);
                break;
            case 6:
                this.raw(PD_P25);
                break;
            case 7:
                this.raw(PD_P37);
                break;
            case 8:
                this.raw(PD_P50);
                break;
            default:
                break;
        }
    }

    private int char_map(char useg, int asciiCode) {
        int retByte = asciiCode;
        int diff = 0;
        if (UNICODE_TO_ASCII.containsKey(retByte)) {
            retByte = UNICODE_TO_ASCII.get(retByte);
//            Log.d(TAG, "USE MAP" + asciiCode + " CHARCODE " + useg);
        }
        if (1280 > retByte && retByte > 1023) {
            diff = (asciiCode - 848);
            retByte = diff;
        }
//        Log.d(TAG, "char_map: IN " + useg + " char_code " + asciiCode + " OUT " + retByte + " RET diff " + String.valueOf(asciiCode - retByte) + " DIFF " + String.valueOf(diff));
        return retByte;
    }

    public byte[] map_convert(String in_string) {
        int size = in_string.length();
        ByteBuffer out_buffer = ByteBuffer.allocate(size + 1);
        for (int i = 0; i < size; i++) {
            int asciiCode = (int) in_string.charAt(i);
            out_buffer.put((byte)char_map(in_string.charAt(i), asciiCode));
        }
        out_buffer.put((byte) 0x0A); // Last byte
        return out_buffer.array();
    }

    public String translate(String word) {
//        if (font_converter.equals("LATIN")) {
//            return translateToLatin(word);
//        } else {
            return word;
//        }
    }

    public String translateToLatin(String word) {
        StringBuilder sb = new StringBuilder();
        for (char c : word.toCharArray()) {
            String conv = String.format("%c", c);
            if (MAP_KRILL_TO_LATIN.containsKey(conv)) {
                sb.append(MAP_KRILL_TO_LATIN.get(conv));
            } else {
                sb.append(conv);
            }
        }
        return sb.toString();
    }

    public void image(Bitmap bitmap, int width, int height) {
        if (this.print_image == true){
            int[] imBorder = checkImageSize(width);
            String imLeft = "";
            String imRight = "";
            StringBuilder pixLine = new StringBuilder();
            int[] imgSize = new int[2];
            imgSize[0] = 0;
            imgSize[1] = 0;

            int colSwitch = 0;

            imLeft = repeatString(imBorder[0], "0");
            imRight = repeatString(imBorder[1], "0");
            int patternLen = pattern.size();

            for (int h = 0; h < height; h++) {
                imgSize[1] += 1;
                pixLine.append(imLeft);
                imgSize[0] += imBorder[0];
                for (int w = 0; w < width; w++) {
                    imgSize[0] += 1;
                    int pixel = bitmap.getPixel(w, h);
                    int redValue = Color.red(pixel);
                    int blueValue = Color.blue(pixel);
                    int greenValue = Color.green(pixel);
                    int alphaValue = Color.alpha(pixel);
                    int imColor = redValue + blueValue + greenValue;
//                str[] pattern = "1X0";


                    if (alphaValue == 0) { // Transparent
                        imColor = 255 * 3;
                    }
                    colSwitch = (colSwitch - 1) * (-1);
                    for (int p = 0; p < patternLen; p++) {
                        String patt = pattern.get(p);
                        if (imColor <= (255 * 3 / patternLen * (p + 1))) {
                            if ("X".equals(patt)) {
                                pixLine.append(colSwitch);
                            } else {
                                pixLine.append(patt);
                            }
                            break;
                        } else if (imColor > (255 * 3 / patternLen * patternLen) && (imColor <= (255 * 3))) {
                            pixLine.append(pattern.get(patternLen - 1));
                            break;
                        }
                    }

                }
                pixLine.append(imRight);
                imgSize[0] += imBorder[1];
            }
            printImage(pixLine.toString(), imgSize);
        }
    }

    private void printImage(String pixLine, int[] imgSize) {
        int i = 0;
        ByteBuffer buffer = ByteBuffer.allocate(pixLine.length() / 8);

        this.raw(S_RASTER_N);

        byte[] ctrl_buffer = new byte[]{(byte) ((imgSize[0] / imgSize[1]) / 8), (byte) 0, (byte) (imgSize[1] & 0xff), (byte) (imgSize[1] >> 8)};
        this.raw(ctrl_buffer);


        while (i < pixLine.length()) {
            int img_byte = Integer.parseInt(pixLine.substring(i, i + 8), 2);  // Binary
            i += 8;
            buffer.put((byte) img_byte);
        }
        this.raw(buffer.array());
    }



    /*
    * ESCPosPrinter.prototype.canvas_to_printer = function(canvas, width, height){
    var ctx = canvas.getContext('2d');
    var im_border = this._check_image_size(width);
    var im_left  = "";
    var im_right = "";
    var pix_line = "";
    var img_size = [0, 0];
    var col_switch = 0;
    // get all canvas pixel data
    im_left = this.repeatString(im_border[0], "0")
    im_right = this.repeatString(im_border[1], "0")

    // Хар цагаан
    for(var y=0; y<height; y++){
      img_size[1] += 1;
      pix_line += im_left;
      img_size[0] += im_border[0];
      for(var x=0; x<width; x++){
        img_size[0] += 1;
        var RGB = ctx.getImageData(x, y, 1, 1).data;
        var im_color = (RGB[0] + RGB[1] + RGB[2]);
        var im_pattern = "1X0";
        var pattern_len = im_pattern.length;

        if (RGB[3] == 0){ // Transparent
          im_color = 255 * 3;
        }
        col_switch = (col_switch - 1) * (-1)
        for(var p=0; p<pattern_len; p++){
          if (im_color <= (255 * 3 / pattern_len * (p+1))) {
            if (im_pattern[p] == "X"){
              pix_line += col_switch;
            }else{
              pix_line += im_pattern[p];
            }
            break;
          }else if(im_color > (255 * 3 / pattern_len * pattern_len) && (im_color <= (255 * 3))) {
            pix_line += im_pattern[-1]
            break;
          }
        }
      }
      pix_line += im_right;
      img_size[0] += im_border[1];
    }
    this._print_image(pix_line, img_size);
  }
    *
    *
    * */

    private int[] checkImageSize(int size) {
        int[] list = new int[2];
        if (size % 32 == 0) {
            list[0] = 0;
            list[1] = 0;
        } else {
            int imageBorder = 32 - (size % 32);
            if ((imageBorder % 2) == 0) {
                list[0] = imageBorder / 2;
                list[1] = imageBorder / 2;
            } else {
                list[0] = Integer.valueOf(imageBorder / 2);
                list[1] = Integer.valueOf(imageBorder / 2) + 1;
            }
        }
        return list;
    }


    private String repeatString(int num, String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) {
            sb.append(str);
        }
        return sb.toString();
    }


    public byte[] prepare() {
        ByteBuffer prep_bytes = ByteBuffer.allocate(total_size);
        for (int i = 0; i < line_buffers.size(); i++) {
            byte[] line_bytes = line_buffers.get(i);
            prep_bytes.put(line_bytes);
        }
//        debug_hex(prep_bytes.array(), "FULL");
        return prep_bytes.array();
    }

    public void debug_hex(byte[] data, String direction) {
        StringBuilder sb = new StringBuilder(data.length * 5);
        for (byte b : data) {
            sb.append(String.format("0x%02x ", b & 0xff));
        }
        System.out.println("[" + direction + "] " + sb.toString());
    }


    public String cutAndRigthFill(String string, int length) {
        if (string.length() < length) {
            int gap = length - string.length();
            for (int i = 0; i < gap; i++) {
                string += " ";
            }
        } else {
            string = string.substring(0, length);
        }
        return string;
    }


    public String leftPadding(String string, int length) {
        if (string.length() < length) {
            int gap = length - string.length();
            for (int i = 0; i < gap; i++) {
                string = " " + string;
            }
        }
        return string;
    }

    public String rightPadding(String string, int length) {
        if (string.length() < length) {
            int gap = length - string.length();
            for (int i = 0; i < gap; i++) {
                string += " ";
            }
        }
        return string;
    }

    public static byte[] getTestData80(String font_convert, int printer_codepage, Activity ac) {
        EscPosPrinter posCommand = new EscPosPrinter(font_convert, printer_codepage);
//        posCommand.set_density(7);
//        posCommand.set_font("A");

        // Set charset
        // 17 Page 17 [PC866: Cyrillic #2]

        posCommand.set_align("CENTER");
        Bitmap largeIcon = BitmapFactory.decodeResource(ac.getResources(), R.mipmap.ic_launcher);

        posCommand.image(largeIcon, largeIcon.getWidth(), largeIcon.getHeight());
        posCommand.text("");
        posCommand.text("Касс: 0031");
        posCommand.set_align("LEFT");
        posCommand.text("ДДТД: 000000000077184567000009908913100");
        posCommand.text("-------------------------------------------");
        posCommand.text("1) 865049819108 : Барааны нэр энд");
        posCommand.text("1500       *15        10%        =123456789");
        posCommand.text("2) 865049819109:Барааны нэр энд");
        posCommand.text("2500       *15        10%        =123456789");
        posCommand.text("3) 865049819110:Барааны нэр энд");
        posCommand.text("3500       *15        10%        =123456789");
        posCommand.text("-------------------------------------------");
        posCommand.set_align("RIGHT");
        posCommand.set_charType("B");
        posCommand.text("НӨАТ ороогүй дүн : 123,456.00");
        posCommand.text("НӨАТатвар   :  12,345.60");
        posCommand.text("НХАТатвар   :  1,234.56");
        posCommand.text("НИЙТ ДҮН: 137,036.16");
        posCommand.set_align("CENTER");
        // TODO : Тун удахгүй
        posCommand.qrcode("4008968969811498142546824452281362396695562036937617327443280005918763034261239397623050659790566988993180435857478227860866529423435428466166386717538542583626604036306095734051793547148162849695935898258671675614361290053235146148604206211419717614895824359463427855189229444944824407643649342069171777408425781539", 300, 300);
        posCommand.text("ЛОТО: BT 77184567");
        posCommand.set_align("LEFT");
        //posCommand.mini_qrcode("000000000077184567000009908913100", BarcodeFormat.CODE_128);
        posCommand.text("Манайхаар үйлчлүүлсэн таньд баярлалаа.");
        posCommand.text("");
        posCommand.text("");
        posCommand.text("");
        posCommand.text("");
        posCommand.cut();
        return posCommand.prepare();
    }
    public static byte[] getTestData55(String font_convert, int printer_codepage, Activity ac) {
        EscPosPrinter posCommand = new EscPosPrinter(font_convert, printer_codepage);
//        posCommand.set_density(7);
        posCommand.set_align("CENTER");

        posCommand.text("");
        posCommand.text("Касс: 0031");
        posCommand.set_align("LEFT");
        posCommand.text("--------------------------------");
        posCommand.text("Бараа       Н/үнэ  Тоо  Дүн     ");
        posCommand.text("--------------------------------");
        posCommand.text("1) 865049819108:Барааны нэр энд");
        posCommand.text("1500    *15   10%    =123456789");
        posCommand.text("2) 865049819109:Барааны нэр энд");
        posCommand.text("2500    *15   10%    =123456789");
        posCommand.text("3) 865049819110:Барааны нэр энд");
        posCommand.text("3500    *15   10%    =123456789");
        posCommand.text("--------------------------------");
        posCommand.text("Дүн : 4 төрөл    1,234,567,890.00");
        posCommand.text(" ДҮН  : 1,234,567,890.00");
        posCommand.text("    НӨАТ   :     4,950,000.00");
        posCommand.set_charType("B");
        posCommand.text("  НИЙТ ДҮН: 1,234,567,890.00");
        posCommand.set_align("CENTER");
        posCommand.qrcode("4008968969811498142546824452281362396695562036937617327443280005918763034261239397623050659790566988993180435857478227860866529423435428466166386717538542583626604036306095734051793547148162849695935898258671675614361290053235146148604206211419717614895824359463427855189229444944824407643649342069171777408425781539", 300, 300);
        posCommand.text("SN: TEL 77184567");
        posCommand.set_align("LEFT");
        posCommand.set_charType("I");
        posCommand.barcode("000000000077184567000009908913100", BarcodeFormat.CODE_128, 500);
        posCommand.text("Манайхаар үйлчлүүлсэн таньд баярлалаа.");

        posCommand.text("");
        posCommand.text("");
        posCommand.text("");
        posCommand.text("");
        posCommand.cut();
        posCommand.eject_cashdrawer();
        return posCommand.prepare();
    }
    public static byte[] getTestFont(){
        EscPosPrinter posCommand = new EscPosPrinter();
        for(int cp=0x01; cp< 47; cp++){
            posCommand.set_codepage(cp);
            posCommand.text("Code Page : " + String.valueOf(cp));
            posCommand.text("Сайн байна уу. АБВГДЕЁЖЗИЙКЛМНОӨРТСУҮПФХЦШЩЪЬЭЮЯ абвгдеёжзийклмноөрстуүпфхцшщъьэюя");
            posCommand.text("1234567890/*-+.,!@#$%^&*()_~");
            posCommand.text("");
        }
        return posCommand.prepare();
    }
    public static byte[] getTestQRCODE(){
        EscPosPrinter posCommand = new EscPosPrinter();
        posCommand.set_align("CENTER");
        posCommand.qrcode("9536099422493770386051439818658381661941807216966935939473390005919036165737445073564581823543570336032877885421561786980267182421819569425932192022252898799589474544278537197200370862161601245947615510737177108739015454553211321746789587917611496557092420686439065394839421076056932292034206794177280713675332601212",300,300);
        posCommand.text("");
        return posCommand.prepare();
    }
    public static byte[] getTestBARCODE(){
        EscPosPrinter posCommand = new EscPosPrinter();
        posCommand.set_align("CENTER");
        posCommand.barcode("ABCDEF0123456789", BarcodeFormat.CODE_128, 500);
        posCommand.text("");
        return posCommand.prepare();
    }
    public static byte[] getTestIMAGE(Activity ac){
        EscPosPrinter posCommand = new EscPosPrinter();
        posCommand.set_align("CENTER");
        Bitmap largeIcon = BitmapFactory.decodeResource(ac.getResources(), R.mipmap.ic_launcher);
        posCommand.image(largeIcon, largeIcon.getWidth(), largeIcon.getHeight());
        posCommand.text("");
        return posCommand.prepare();
    }
    public void qrcode(String qrcode,int w,int h){
        int width = w;//300
        int heigth = h;
        int spaceW = 26;
        int spaceH = 26;

        if(!TextUtils.isEmpty(qrcode)){


            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix bm = null;
            try {
                bm = writer.encode(qrcode, BarcodeFormat.QR_CODE, width, heigth);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            Bitmap ImageBitmap = Bitmap.createBitmap(width-spaceW*2, heigth-spaceH*2, Bitmap.Config.ARGB_8888);

            for (int i = spaceW; i < width-spaceW; i++) {//width
                for (int j = spaceH; j < heigth-spaceH; j++) {//height
                    ImageBitmap.setPixel(i-spaceW, j-spaceH, bm.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
            image(ImageBitmap, ImageBitmap.getWidth(), ImageBitmap.getHeight());
        }
    }

    public void mini_qrcode(String qrcode,BarcodeFormat format){
        int width = 150;//300
        int heigth = 150;
        int width2 = 250;//300
        int heigth2 = 120;//300

        if(!TextUtils.isEmpty(qrcode)){
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix bm = null;
            BitMatrix bm2 = null;
            try {
                bm = writer.encode(qrcode, BarcodeFormat.QR_CODE, width, heigth);
                bm2 = writer.encode(qrcode, format, width2, heigth2);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            int spaceW = 20;
            int spaceH = 20;
            Bitmap ImageBitmap = Bitmap.createBitmap(width+width2-spaceW*2, heigth-spaceH*2, Bitmap.Config.ARGB_8888);
            for (int i = spaceW ; i < width + width2 - spaceW; i++) {//width
                for (int j = spaceH; j < heigth-spaceH; j++) {//height
                    if (i < width)
                        ImageBitmap.setPixel(i-spaceW, j-spaceH, bm.get(i, j) ? Color.BLACK : Color.WHITE);
                    else {
                        if(i - width <= width2 && j >= spaceH*2 && j < heigth2+spaceH*2 )
                            ImageBitmap.setPixel(i-spaceW*2, j-spaceH*2, bm2.get(i - width, j-spaceH*2) ? Color.BLACK : Color.WHITE);
                    }
                }
            }

            image(ImageBitmap, ImageBitmap.getWidth(), ImageBitmap.getHeight());
        }
    }

    public void barcode(String barcode, BarcodeFormat format, int width){
        int heigth = 50;

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix bm = null;
        try {
            bm = writer.encode(barcode, format, width, heigth);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        Bitmap ImageBitmap = Bitmap.createBitmap(width, heigth, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < width; i++) {//width
            for (int j = 0; j < heigth; j++) {//height
                ImageBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
            }
        }

        image(ImageBitmap, width, heigth);
    }

}