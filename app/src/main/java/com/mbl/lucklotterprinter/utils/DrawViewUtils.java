package com.mbl.lucklotterprinter.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.mbl.lucklotterprinter.R;
import com.mbl.lucklotterprinter.model.LineModel;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DrawViewUtils {
    private static final float TEXT_SIZE_HEADER = 14.f;
    private static final int SIZE_SCALE = 3;
    private static final int MARGIN_BIG = 15;
    private static final int MARGIN_SMALL = 7;
    private static final float TEXT_SIZE_FOOTER = 12.f;
    private static final float TEXT_SIZE_BODY = 18.0f;
    private Activity mContext;
    private float mGestureThreshold;

    public DrawViewUtils(Context c) {
        mContext = (Activity) c;

// Convert the dips to pixels
        final float scale = mContext.getResources().getDisplayMetrics().density;
        mGestureThreshold = (int) (TEXT_SIZE_BODY * scale + 0.5f);
    }


    public static boolean saveImage(Bitmap bitmap, String filePath, String filename, Bitmap.CompressFormat format,
                                    int quality) {
        if (quality > 100) {
            Log.d("saveImage", "quality cannot be greater that 100");
            return false;
        }
        File file;
        FileOutputStream out = null;
        try {
            switch (format) {
                case PNG:
                    file = new File(filePath, filename);
                    out = new FileOutputStream(file);
                    return bitmap.compress(Bitmap.CompressFormat.PNG, quality, out);
                case JPEG:
                    file = new File(filePath, filename);
                    out = new FileOutputStream(file);
                    return bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
                default:
                    file = new File(filePath, filename);
                    out = new FileOutputStream(file);
                    return bitmap.compress(Bitmap.CompressFormat.PNG, quality, out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public Bitmap processingBitmap(Uri source1) {
        Bitmap bm1 = null;
        Bitmap newBitmap = null;

        try {
            bm1 = BitmapFactory.decodeStream(mContext.
                    getContentResolver().openInputStream(source1));

            Bitmap.Config config = bm1.getConfig();
            if (config == null) {
                config = Bitmap.Config.ARGB_8888;
            }

            newBitmap = Bitmap.createBitmap(bm1.getWidth(), bm1.getHeight(), config);

            Canvas newCanvas = new Canvas(newBitmap);

            newCanvas.drawBitmap(bm1, 0, 0, null);


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            newBitmap = rotateImageIfRequired(newBitmap, source1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newBitmap;
    }

    private Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        if (selectedImage.getScheme().equals("content")) {
            String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
            Cursor c = mContext.getContentResolver().query(selectedImage, projection, null, null, null);
            if (c.moveToFirst()) {
                final int rotation = c.getInt(0);
                c.close();
                return rotateImage(img, rotation);
            }
            return img;
        } else {
            ExifInterface ei = new ExifInterface(selectedImage.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        }
    }

    private Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
    }

    public Bitmap processingBitmapBefore(Uri source, String gameName, List<LineModel> listTicketPrint, String date, String drawCode, String orderCode) {
        Bitmap bm1 = null;
        Bitmap newBitmap = null;
        try {
            bm1 = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(source));

            bm1 = Bitmap.createScaledBitmap(bm1, (int) (bm1.getWidth() / SIZE_SCALE), (int) (bm1.getHeight() / SIZE_SCALE), true);
           /* Bitmap.Config config = bm1.getConfig();
            if (config == null) {
                config = Bitmap.Config.ARGB_8888;
            }*/
            // newBitmap = Bitmap.createBitmap(bm1.getWidth(), bm1.getHeight(), config);
            //Canvas newCanvas = new Canvas(newBitmap);
            // newCanvas.drawBitmap(bm1, 0, 0, null);
            try {
                newBitmap = rotateImageIfRequired(bm1, source);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Canvas newCanvas = new Canvas(newBitmap);
            newCanvas.drawBitmap(newBitmap, 0, 0, null);

            drawDetail(gameName, newCanvas, date, listTicketPrint, drawCode, orderCode);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return newBitmap;
    }

    public Bitmap processingBitmapKeno(Uri source, String productName, List<LineModel> listTicketPrint, String date, String kyquay, String orderCode, String pos, String denKy) {
        Bitmap bm1 = null;
        Bitmap newBitmap = null;
        try {
            bm1 = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(source));
            bm1 = Bitmap.createScaledBitmap(bm1, (int) (bm1.getWidth() / SIZE_SCALE), (int) (bm1.getHeight() / SIZE_SCALE), true);
            try {
                newBitmap = rotateImageIfRequired(bm1, source);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Canvas newCanvas = new Canvas(newBitmap);
            newCanvas.drawBitmap(newBitmap, 0, 0, null);

            drawDetailKeno(productName, newCanvas, date, listTicketPrint, kyquay, orderCode, pos, denKy);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return newBitmap;
    }


    private void drawDetail(String gameName, Canvas newCanvas, String date, List<LineModel> listTicketPrint, String drawCode, String orderCode) {

        float height = 0;
        String textDraw = "";

//        if (!TextUtils.isEmpty(orderCode)) {
//            textDraw = "#" + orderCode;
//            final float scale = mContext.getResources().getDisplayMetrics().density;
//            int fontsize = (int) (TEXT_SIZE_HEADER * scale + 0.5f);
//            Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
//            paintText.setTextSize(fontsize);
//            paintText.setStyle(Paint.Style.FILL);
//            paintText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//            paintText.setColor(mContext.getResources().getColor(R.color.white50));
//            Rect rectText = new Rect();
//            paintText.getTextBounds(textDraw, 0, textDraw.length(), rectText);
//            float top = 0;
//            float margin = MARGIN_BIG;
//            float bottom = rectText.height();
//            int floatx = newCanvas.getWidth() / 2;
//            newCanvas.drawRect(floatx - rectText.width() - 2 * margin, top,
//                    floatx, bottom + margin, paintText);
//            paintText.setColor(mContext.getResources().getColor(R.color.black));
//
////            int iii = rectText.width();
//            newCanvas.drawText(textDraw,
//                    floatx - rectText.width() - margin, bottom + margin / 2, paintText);
//        }

        if (!TextUtils.isEmpty(orderCode)) {
            textDraw = "#" + orderCode;
            final float scale = mContext.getResources().getDisplayMetrics().density;
            int fontsize = (int) (TEXT_SIZE_HEADER * scale + 0.5f);
            Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintText.setTextSize(fontsize);
            paintText.setStyle(Paint.Style.FILL);
            paintText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paintText.setColor(mContext.getResources().getColor(R.color.white50));
            Rect rectText = new Rect();
            paintText.getTextBounds(textDraw, 0, textDraw.length(), rectText);
            float top = 0;
            float margin = MARGIN_BIG;
            float bottom = rectText.height();
            int corner = 20;
            newCanvas.drawRect(newCanvas.getWidth() - corner - rectText.width() - 2 * margin, top,
                    newCanvas.getWidth(), bottom + margin, paintText);
            paintText.setColor(mContext.getResources().getColor(R.color.black));

            newCanvas.drawText(textDraw,
                    newCanvas.getWidth() - corner - rectText.width() - margin, bottom + margin / 2, paintText);
        }

        if (!TextUtils.isEmpty(date)) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("sun", "Chủ nhật");
            hashMap.put("mon", "Thứ hai");
            hashMap.put("tue", "Thứ ba");
            hashMap.put("wed", "Thứ tư");
            hashMap.put("thu", "Thứ năm");
            hashMap.put("fri", "Thứ sáu");
            hashMap.put("sat", "Thứ bảy");
            Calendar calendar = Calendar.getInstance();
            String[] dates = date.split(";");
            String[] kyquays = drawCode.split(";");
            List<String> list = new ArrayList<>();
            if (dates.length == 1) {
                calendar.setTime(DateTimeUtils.convertStringToDate(dates[0], DateTimeUtils.SIMPLE_DATE_FORMAT));
                String dayofweek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);
                String dayVn = hashMap.get(dayofweek.toLowerCase());
                list.add("Kỳ #" + drawCode + ", Ngày quay: " + dates[0]);
            } else {
                calendar.setTime(DateTimeUtils.convertStringToDate(dates[1], DateTimeUtils.SIMPLE_DATE_FORMAT));
                String dayofweek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);
                String dayVn = hashMap.get(dayofweek.toLowerCase());
                list.add("Đến kỳ #" + kyquays[1] + ", Ngày quay: " + dates[1]);
                calendar.setTime(DateTimeUtils.convertStringToDate(dates[0], DateTimeUtils.SIMPLE_DATE_FORMAT));
                dayofweek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);
                String dayVn2 = hashMap.get(dayofweek.toLowerCase());
                list.add("Từ kỳ #" + kyquays[0] + ", Ngày quay: " + dates[0]);
            }
            // textDraw = "#" + kyquay + ", Ngày quay: " + date;
            for (String item : list) {
                final float scale = mContext.getResources().getDisplayMetrics().density;
                int fontsize = (int) (TEXT_SIZE_FOOTER * scale + 0.5f);
                Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintText.setTextSize(fontsize);
                paintText.setStyle(Paint.Style.FILL);
                paintText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                paintText.setColor(mContext.getResources().getColor(R.color.white50));
                Rect rectText = new Rect();
                paintText.getTextBounds(item, 0, item.length(), rectText);
                int margin = MARGIN_SMALL;
                float top = height + margin + rectText.height();
                float bottom = height;
                top = newCanvas.getHeight() - top;
                bottom = newCanvas.getHeight() - bottom;
                newCanvas.drawRect(0, top,
                        newCanvas.getWidth(), bottom, paintText);
                paintText.setColor(mContext.getResources().getColor(R.color.black));
                newCanvas.drawText(item,
                        (newCanvas.getWidth() / 2) - (rectText.width() / 2), bottom - margin, paintText);
                height += rectText.height() + margin;
            }
        }
        for (int i = listTicketPrint.size() - 1; i >= 0; i--) {
           /* if (i < listTicketPrint.size() - 1)
                break;*/
            LineModel itemTicket = listTicketPrint.get(i);

            StringBuilder boso = new StringBuilder();
            switch (i) {
                case 0:
                    boso = new StringBuilder("A: ");
                    break;
                case 1:
                    boso = new StringBuilder("B: ");
                    break;
                case 2:
                    boso = new StringBuilder("C: ");
                    break;
                case 3:
                    boso = new StringBuilder("D: ");
                    break;
                case 4:
                    boso = new StringBuilder("E: ");
                    break;
                case 5:
                    boso = new StringBuilder("F: ");
                    break;
            }

            String[] arrLine = itemTicket.getLine().split(",");
            for (int i1 = 0; i1 < arrLine.length; i1++) {
                boso.append(StringUtils.leftPad(arrLine[i1], 2, '0')).append(" ");
            }
            if (itemTicket.getType().equals(Constants.TICKET_NO_AMOUNT)) {
                textDraw = boso.toString();
            } else {
                textDraw = boso + "    " + NumberUtils.formatPriceNumber(itemTicket.getAmount());
            }

            Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintText.setTextSize(mGestureThreshold);
            paintText.setStyle(Paint.Style.FILL);
            paintText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paintText.setColor(mContext.getResources().getColor(R.color.white50));
            Rect rectText = new Rect();
            int margin = MARGIN_BIG;
            paintText.getTextBounds(textDraw, 0, textDraw.length(), rectText);
            float top = height + margin + rectText.height();
            float bottom = height;
            top = newCanvas.getHeight() - top;
            bottom = newCanvas.getHeight() - bottom;

            if (i == 0) {
                top = top - margin;
            }
            newCanvas.drawRect(0, top,
                    newCanvas.getWidth(), bottom, paintText);

            paintText.setColor(mContext.getResources().getColor(R.color.black));

            String row1 = boso.toString();
            String row2 = "";
            int x = 100;

            if (itemTicket.getProductID() == Constants.PRODUCT_MAX3D_PRO) {
                if (boso.length() > 46) {
                    row1 = boso.substring(0, 46);
                    row2 = (boso.substring(46)).trim();
                } else {
                    row1 = " " + boso;
                }
            } else {
                if (boso.length() > 38) {
                    row1 = boso.substring(0, 38);
                    row2 = "      " + (boso.substring(38)).trim();
                } else {
                    row1 = "  " + boso;
                }
            }

            if (!TextUtils.isEmpty(row2)) {
                x = 10;
                newCanvas.drawText(row2,
                        x, bottom - margin, paintText);

                if (itemTicket.getType().equals(Constants.TICKET_SHOW_AMOUNT)) {
                    paintText.setColor(mContext.getResources().getColor(R.color.colorPrimary));
                    Rect rectTextTem = new Rect();
                    paintText.getTextBounds(row2, 0, row2.length(), rectTextTem);
                    String textAmount = NumberUtils.formatPriceNumber(itemTicket.getAmount());
                    newCanvas.drawText(textAmount,
                            x + rectTextTem.width() + 100, bottom - margin, paintText);
                }

                height += rectText.height() + margin;

                bottom = height;
                bottom = newCanvas.getHeight() - bottom;
                top = height + margin + rectText.height();
                top = newCanvas.getHeight() - top;

                paintText.setColor(mContext.getResources().getColor(R.color.white50));
                if (i == 0) {
                    top = top - margin;
                    bottom = bottom - margin;
                }
                newCanvas.drawRect(0, top,
                        newCanvas.getWidth(), bottom, paintText);
                paintText.setColor(mContext.getResources().getColor(R.color.black));
                if (i == 0) {
                    bottom = bottom + margin;
                }
                newCanvas.drawText(row1,
                        x, bottom - margin, paintText);
            } else {
                newCanvas.drawText(boso.toString(),
                        x, bottom - margin, paintText);

                if (itemTicket.getType().equals(Constants.TICKET_SHOW_AMOUNT)) {
                    String textAmount = NumberUtils.formatPriceNumber(itemTicket.getAmount());
                    if(itemTicket.getProductID() == Constants.PRODUCT_MAX3D_PRO && itemTicket.getItemType() == 2)
                        textAmount = "X" + itemTicket.getSystematic() + "  " + textAmount;
                    paintText.setColor(mContext.getResources().getColor(R.color.colorPrimary));
                    Rect rectTextTem = new Rect();
                    String text1 = textDraw.replace(textAmount, "").trim();
                    paintText.getTextBounds(text1, 0, text1.length(), rectTextTem);
                    newCanvas.drawText(textAmount,
                            x + rectTextTem.width() + 100, bottom - margin, paintText);
                }

            }



            height += rectText.height() + margin;

        }
        if (gameName != null) {
            textDraw = gameName;

            Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);

            paintText.setTextSize(mGestureThreshold);
            paintText.setStyle(Paint.Style.FILL);
            paintText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paintText.setColor(mContext.getResources().getColor(R.color.white50));
            Rect rectText = new Rect();
            paintText.getTextBounds(textDraw, 0, textDraw.length(), rectText);
            int margin = MARGIN_BIG;
            float top = height + margin + rectText.height();
            float bottom = height;
            top = newCanvas.getHeight() - top - margin;
            bottom = newCanvas.getHeight() - bottom - margin;
            newCanvas.drawRect(0, top,
                    newCanvas.getWidth(), bottom, paintText);
            paintText.setColor(mContext.getResources().getColor(R.color.colorPrimary));


            newCanvas.drawText(textDraw,
                    (newCanvas.getWidth() / 2) - (rectText.width() / 2), bottom - margin, paintText);
        }
    }

    private void drawDetailKeno(String productName, Canvas newCanvas, String date, List<LineModel> listTicketPrint, String kyquay, String orderCode, String pos, String denKy) {

        float height = 0;
        String textDraw = "";

        if (!TextUtils.isEmpty(pos)) {
            textDraw = pos;
            final float scale = mContext.getResources().getDisplayMetrics().density;
            int fontsize = (int) (TEXT_SIZE_HEADER * scale + 0.5f);
            Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintText.setTextSize(fontsize);
            paintText.setStyle(Paint.Style.FILL);
            paintText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paintText.setColor(mContext.getResources().getColor(R.color.white50));
            Rect rectText = new Rect();
            paintText.getTextBounds(textDraw, 0, textDraw.length(), rectText);
            float top = 0;
            float margin = MARGIN_BIG;
            float bottom = rectText.height();
            newCanvas.drawRect(0, top,
                    rectText.width() + 2 * margin, bottom + margin / 2, paintText);
            paintText.setColor(mContext.getResources().getColor(R.color.black));
            newCanvas.drawText(textDraw,
                    margin, bottom, paintText);
        }
        if (!TextUtils.isEmpty(orderCode)) {
            textDraw = "#" + orderCode;
            final float scale = mContext.getResources().getDisplayMetrics().density;
            int fontsize = (int) (TEXT_SIZE_HEADER * scale + 0.5f);
            Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintText.setTextSize(fontsize);
            paintText.setStyle(Paint.Style.FILL);
            paintText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paintText.setColor(mContext.getResources().getColor(R.color.white50));
            Rect rectText = new Rect();
            paintText.getTextBounds(textDraw, 0, textDraw.length(), rectText);
            float top = 0;
            float margin = MARGIN_BIG;
            float bottom = rectText.height();
            newCanvas.drawRect(newCanvas.getWidth() - rectText.width() - 2 * margin, top,
                    newCanvas.getWidth(), bottom + margin, paintText);
            paintText.setColor(mContext.getResources().getColor(R.color.black));
            newCanvas.drawText(textDraw,
                    newCanvas.getWidth() - rectText.width() - margin, bottom + margin / 2, paintText);
        }
        if (!TextUtils.isEmpty(date)) {
           /* HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("sun", "Chủ nhật");
            hashMap.put("mon", "Thứ hai");
            hashMap.put("tue", "Thứ ba");
            hashMap.put("wed", "Thứ tư");
            hashMap.put("thu", "Thứ năm");
            hashMap.put("fri", "Thứ sáu");
            hashMap.put("sat", "Thứ bảy");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateTimeUtils.convertStringToDate(date, DateTimeUtils.SIMPLE_DATE_FORMAT));
            String dayofweek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);
            String dayVn = hashMap.get(dayofweek.toLowerCase());*/
            textDraw = "Kỳ từ #" + kyquay + " đến #" + denKy + ", Ngày quay: " + date;
            // textDraw = "#" + kyquay + ", Ngày quay: " + date;
            final float scale = mContext.getResources().getDisplayMetrics().density;
            int fontsize = (int) (TEXT_SIZE_FOOTER * scale + 0.5f);
            Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintText.setTextSize(fontsize);
            paintText.setStyle(Paint.Style.FILL);
            paintText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paintText.setColor(mContext.getResources().getColor(R.color.white50));
            Rect rectText = new Rect();
            paintText.getTextBounds(textDraw, 0, textDraw.length(), rectText);
            int margin = MARGIN_SMALL;
            float top = height + margin + rectText.height();
            float bottom = height;
            top = newCanvas.getHeight() - top;
            bottom = newCanvas.getHeight() - bottom;
            newCanvas.drawRect(0, top,
                    newCanvas.getWidth(), bottom, paintText);
            paintText.setColor(mContext.getResources().getColor(R.color.black));
            newCanvas.drawText(textDraw,
                    (newCanvas.getWidth() / 2) - (rectText.width() / 2), bottom - margin, paintText);
            height += rectText.height() + margin;
        }
        for (int i = listTicketPrint.size() - 1; i >= 0; i--) {
           /* if (i < listTicketPrint.size() - 1)
                break;*/
            LineModel itemTicket = listTicketPrint.get(i);
            String stt = "A:";
            switch (i) {
                case 0:
                    stt = "A:";
                    break;
                case 1:
                    stt = "B:";
                    break;
                case 2:
                    stt = "C:";
                    break;
                case 3:
                    stt = "D:";
                    break;
                case 4:
                    stt = "E:";
                    break;
                case 5:
                    stt = "F:";
                    break;
            }
            String boso = itemTicket.getTitle().replaceAll(" ", "  ");
            textDraw = stt + "  " + boso + "    " + itemTicket.getAmount();
            Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintText.setTextSize(mGestureThreshold);
            paintText.setStyle(Paint.Style.FILL);
            paintText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paintText.setColor(mContext.getResources().getColor(R.color.white50));
            Rect rectText = new Rect();
            int margin = MARGIN_BIG;
            paintText.getTextBounds(textDraw, 0, textDraw.length(), rectText);
            float top = height + margin + rectText.height();
            float bottom = height;
            top = newCanvas.getHeight() - top;
            bottom = newCanvas.getHeight() - bottom;

            if (i == 0) {
                top = top - margin;
            }
            newCanvas.drawRect(0, top,
                    newCanvas.getWidth(), bottom, paintText);

            paintText.setColor(mContext.getResources().getColor(R.color.black));

            String row1 = "";
            String row2 = "";
            if (boso.length() > 19) {
                row1 = stt + "  " + boso.substring(0, 19);
                row2 = "      " + (boso.substring(19)).trim();
            } else {
                row1 = stt + "  " + boso;
            }
            if (!TextUtils.isEmpty(row2)) {
                newCanvas.drawText(row2,
                        100, bottom - margin, paintText);
                height += rectText.height() + margin;

                bottom = height;
                bottom = newCanvas.getHeight() - bottom;
                top = height + margin + rectText.height();
                top = newCanvas.getHeight() - top;

                paintText.setColor(mContext.getResources().getColor(R.color.white50));
                if (i == 0) {
                    top = top - margin;
                    bottom = bottom - margin;
                }
                newCanvas.drawRect(0, top,
                        newCanvas.getWidth(), bottom, paintText);
                paintText.setColor(mContext.getResources().getColor(R.color.black));
                if (i == 0) {
                    bottom = bottom + margin;
                }
                newCanvas.drawText(row1,
                        100, bottom - margin, paintText);
            } else {

                newCanvas.drawText(row1,
                        100, bottom - margin, paintText);
            }

            paintText.setColor(mContext.getResources().getColor(R.color.colorPrimary));
            Rect rectTextTem = new Rect();
            String text1 = "07  12  20  33  36";//textDraw.replace(itemTicket.getValue(), "").trim();
            // text1= text1.substring(text1.length()/2);
            paintText.getTextBounds(text1, 0, text1.length(), rectTextTem);
            newCanvas.drawText(String.valueOf(itemTicket.getAmount()),
                    100 + rectTextTem.width() + 100, bottom - margin, paintText);

            height += rectText.height() + margin;
        }
        if (productName != null) {
            textDraw = productName;
            Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);

            paintText.setTextSize(mGestureThreshold);
            paintText.setStyle(Paint.Style.FILL);
            paintText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paintText.setColor(mContext.getResources().getColor(R.color.white50));
            Rect rectText = new Rect();
            paintText.getTextBounds(textDraw, 0, textDraw.length(), rectText);
            int margin = MARGIN_BIG;
            float top = height + margin + rectText.height();
            float bottom = height;
            top = newCanvas.getHeight() - top - margin;
            bottom = newCanvas.getHeight() - bottom - margin;
            newCanvas.drawRect(0, top,
                    newCanvas.getWidth(), bottom, paintText);
            paintText.setColor(mContext.getResources().getColor(R.color.colorPrimary));


            newCanvas.drawText(textDraw,
                    (newCanvas.getWidth() / 2) - (rectText.width() / 2), bottom - margin, paintText);
        }
    }


    public Bitmap processingBitmapAfter(Uri source, String fullName, String pidNumber, String mobileNumber) {
        Bitmap bm1 = null;
        Bitmap newBitmap = null;

        try {
            // bm1 = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(source));
            bm1 = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(source));
            bm1 = Bitmap.createScaledBitmap(bm1, (int) (bm1.getWidth() / SIZE_SCALE), (int) (bm1.getHeight() / SIZE_SCALE), true);
           /* Bitmap.Config config = bm1.getConfig();
            if (config == null) {
                config = Bitmap.Config.ARGB_8888;
            }
            newBitmap = Bitmap.createBitmap(bm1.getWidth(), bm1.getHeight(), config);
            Canvas newCanvas = new Canvas(newBitmap);
            newCanvas.drawBitmap(bm1, 0, 0, null);*/
            try {
                newBitmap = rotateImageIfRequired(bm1, source);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Canvas newCanvas = new Canvas(newBitmap);
            newCanvas.drawBitmap(newBitmap, 0, 0, null);

            String textDraw = "";
            int height = 0;

            if (!TextUtils.isEmpty(mobileNumber)) {
                textDraw = "Điện thoại: ";
                Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintText.setTextSize(mGestureThreshold);
                paintText.setStyle(Paint.Style.FILL);
                // paintText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                paintText.setColor(mContext.getResources().getColor(R.color.white50));
                Rect rectText = new Rect();
                paintText.getTextBounds(textDraw, 0, textDraw.length(), rectText);
                int margin = MARGIN_BIG;
                float top = height + margin + rectText.height();
                float bottom = height;
                top = newCanvas.getHeight() - top;
                bottom = newCanvas.getHeight() - bottom;
                newCanvas.drawRect(0, top,
                        newCanvas.getWidth(), bottom, paintText);
                paintText.setColor(mContext.getResources().getColor(R.color.black));
                newCanvas.drawText(textDraw,
                        100, bottom - margin, paintText);
                paintText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                newCanvas.drawText(mobileNumber,
                        100 + rectText.width() + 50, bottom - margin, paintText);
                height += rectText.height() + margin;
            }
            if (!TextUtils.isEmpty(pidNumber)) {
                textDraw = "Số CMND: ";
                Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintText.setTextSize(mGestureThreshold);
                paintText.setStyle(Paint.Style.FILL);
                // paintText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                paintText.setColor(mContext.getResources().getColor(R.color.white50));
                Rect rectText = new Rect();
                paintText.getTextBounds(textDraw, 0, textDraw.length(), rectText);
                int margin = MARGIN_BIG;
                float top = height + margin + rectText.height();
                float bottom = height;
                top = newCanvas.getHeight() - top;
                bottom = newCanvas.getHeight() - bottom;
                newCanvas.drawRect(0, top,
                        newCanvas.getWidth(), bottom, paintText);
                paintText.setColor(mContext.getResources().getColor(R.color.black));
                newCanvas.drawText(textDraw,
                        100, bottom - margin, paintText);
                paintText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                newCanvas.drawText(pidNumber,
                        100 + rectText.width() + 50, bottom - margin, paintText);
                height += rectText.height() + margin;
            }
            if (!TextUtils.isEmpty(fullName)) {
                textDraw = "Người nhận: ";
                Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintText.setTextSize(mGestureThreshold);
                paintText.setStyle(Paint.Style.FILL);
                // paintText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                paintText.setColor(mContext.getResources().getColor(R.color.white50));
                Rect rectText = new Rect();
                paintText.getTextBounds(textDraw, 0, textDraw.length(), rectText);
                int margin = MARGIN_BIG;
                float top = height + margin + rectText.height();
                float bottom = height;
                top = newCanvas.getHeight() - top;
                bottom = newCanvas.getHeight() - bottom;
                newCanvas.drawRect(0, top,
                        newCanvas.getWidth(), bottom, paintText);
                paintText.setColor(mContext.getResources().getColor(R.color.black));
                newCanvas.drawText(textDraw,
                        100, bottom - margin, paintText);
                paintText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                newCanvas.drawText(fullName,
                        100 + rectText.width() + 50, bottom - margin, paintText);
                height += rectText.height() + margin;
            }

            /*Rect rectangle = new Rect(
                    0, // Left
                    0, // Top
                    newCanvas.getWidth(), // Right
                    newCanvas.getHeight() / 7 // Bottom
            );
            newCanvas.drawRect(rectangle, paint);
            newCanvas.translate(20, 100);
            String textDraw = "Người nhận: " + fullName;//+ "\nSố CMND: " + pidNumber + "\nĐiện thoại: " + mobileNumber;
            CharSequence str = TextUtils.ellipsize(textDraw, mTextPaint, newCanvas.getWidth(), TextUtils.TruncateAt.END);
            SpannableString wordToSpan = new SpannableString(str);
            int lengh1 = ("Người nhận: ").length();
            int lengh2 = ("Người nhận: " + fullName).length();
            wordToSpan.setSpan(new StyleSpan(Typeface.NORMAL), 0, lengh1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), lengh1, lengh2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            StaticLayout staticLayout = new StaticLayout(wordToSpan, mTextPaint, newCanvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
            staticLayout.draw(newCanvas);
            newCanvas.translate(0, 100);

            textDraw = "Số CMND: " + pidNumber;
            str = TextUtils.ellipsize(textDraw, mTextPaint, newCanvas.getWidth(), TextUtils.TruncateAt.END);
            wordToSpan = new SpannableString(str);
            lengh1 = ("Số CMND: ").length();
            lengh2 = ("Số CMND: " + pidNumber).length();
            wordToSpan.setSpan(new StyleSpan(Typeface.NORMAL), 0, lengh1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), lengh1, lengh2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            staticLayout = new StaticLayout(wordToSpan, mTextPaint, newCanvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
            staticLayout.draw(newCanvas);
            newCanvas.translate(0, 100);

            textDraw = "Điện thoại: " + mobileNumber;
            str = TextUtils.ellipsize(textDraw, mTextPaint, newCanvas.getWidth(), TextUtils.TruncateAt.END);
            wordToSpan = new SpannableString(str);
            lengh1 = ("Điện thoại: ").length();
            lengh2 = ("Điện thoại: " + mobileNumber).length();
            wordToSpan.setSpan(new StyleSpan(Typeface.NORMAL), 0, lengh1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), lengh1, lengh2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            staticLayout = new StaticLayout(wordToSpan, mTextPaint, newCanvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
            staticLayout.draw(newCanvas);
            newCanvas.translate(0, 100);*/


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return newBitmap;
    }
}
