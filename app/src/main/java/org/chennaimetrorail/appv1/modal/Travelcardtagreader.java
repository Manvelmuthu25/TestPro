package org.chennaimetrorail.appv1.modal;

import android.content.Intent;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.AccessControlException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.concurrent.RecursiveTask;


public class Travelcardtagreader {


    private static final String TAG = "MainActivity";

    private static final String[] TECHES = new String[]{
            NfcA.class.getName(), NfcB.class.getName(), NfcF.class.getName(), NfcV.class.getName(),
            IsoDep.class.getName(), Ndef.class.getName(), NdefFormatable.class.getName(), MifareClassic.class.getName(), MifareUltralight.class.getName()};
    private static final byte GET_ADDITIONAL_FRAME = (byte) 0xAF;
    private static final byte GET_APPLICATION_DIRECTORY = (byte) 0x6A;
    private static final byte SELECT_APPLICATION = (byte) 0x5A;
    private static final byte READ_DATA = (byte) 0xBD;
    private static final byte READ_RECORD = (byte) 0xBB;
    private static final byte GET_VALUE = (byte) 0x6C;
    private static final byte GET_FILES = (byte) 0x6F;
    private static final byte GET_FILE_SETTINGS = (byte) 0xF5;

    // Status codes (Section 3.4)
    private static final byte OPERATION_OK = (byte) 0x00;
    private static final byte PERMISSION_DENIED = (byte) 0x9D;
    private static final byte AUTHENTICATION_ERROR = (byte) 0xAE;
    private static final byte ADDITIONAL_FRAME = (byte) 0xAF;

    private static byte[] appId = new byte[3];

    private static IsoDep isoDep;


    private static byte[] wrapMessage(byte command, byte[] parameters) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        stream.write((byte) 0x90);
        stream.write(command);
        stream.write((byte) 0x00);
        stream.write((byte) 0x00);
        if (parameters != null) {
            stream.write((byte) parameters.length);
            stream.write(parameters);
        }

        stream.write((byte) 0x00);

        return stream.toByteArray();
    }


    public static String[] ShowCardInformation(Tag tag, Intent intent) {
        String[] cardInformation = new String[32];
        String cardID = "";
        String cardType = "";
        int arrIndex = 0;
        byte[] result = new byte[0];

        if (tag != null) {
            StringBuilder stringBuilder = new StringBuilder();

            isoDep = IsoDep.get(tag);
            byte[] tagId = tag.getId();

            try {
                isoDep.connect();

                int[] arrAppId = new int[1];
                byte[] appId = new byte[]{1, 0, 0};
                String fileContents1 = "";
                String fileContents7 = "";
                String fileContents8 = "";
                String[] file1Contents = new String[0];
                String[] file7Contents = new String[0];
                String[] file8Contents = new String[0];
                String fileContents10 = "";
                String[] file10Contents = new String[0];


                arrAppId[0] = ByteUtils.byteArrayToInt(appId);



                for (int appIdLoop : arrAppId) {
                    byte[] appIdBuff = new byte[3];
                    appIdBuff[0] = (byte) ((appIdLoop & 0xFF0000) >> 16);
                    appIdBuff[1] = (byte) ((appIdLoop & 0xFF00) >> 8);
                    appIdBuff[2] = (byte) (appIdLoop & 0xFF);

                    try {
                        result = sendRequest(SELECT_APPLICATION, appIdBuff);

                        fileContents1 = "";
                        fileContents7 = "";
                        fileContents8 = "";
                        fileContents10 = "";

                        try {
                            byte[] file1Data = readFile(1);
                            fileContents1 = ByteArrayToString(file1Data);
                            file1Contents = ByteArrayToStringArray(file1Data);
                        } catch (Exception e) {
                            // handle the file read failure

                        }

                        try {
                            byte[] file7Data = readRecord(7);
                            fileContents7 = ByteArrayToString(file7Data);
                            file7Contents = ByteArrayToStringArray(file7Data);
                        } catch (Exception e) {
                            // handle the file read failure

                        }

                        try {
                            byte[] file8Data = readRecord(8);
                            fileContents8 = ByteArrayToString(file8Data);
                            file8Contents = ByteArrayToStringArray(file8Data);
                        } catch (Exception e) {
                            // handle the file read failure
                        }

                        try {
                            byte[] file10Data = readFile(10);
                            fileContents10 = ByteArrayToString(file10Data);
                            file10Contents = ByteArrayToStringArray(file10Data);
                        } catch (Exception e) {
                            // handle the file read failure
                        }


                        cardInformation[0] = CalculateBalance(file7Contents, file8Contents);

                        if (!fileContents1.equals("")) {
                            cardID = (fileContents1.replace(" ", "")).substring(16, 26);
                            Log.d("fileContents1 ","" + fileContents1);
                            Log.d("cardID ","" + cardID);
                        }


                        cardType = "";
                        if (!fileContents10.equals("")) {
                            Log.d("fileContents10 ", "CVal: " + (fileContents10.replace(" ", "")).substring(4, 6));
                            cardType = GetTypeOfTicket((fileContents10.replace(" ", "")).substring(4, 6));
                            Log.d("fileContents10 ", "" + fileContents10);
                            Log.d("fileContents10 ", "CType : " + cardType);
                        } else {
                            Log.d("fileContents10 ", "file10 is empty" );
                        }

                        cardInformation[1] = cardID + ":" + cardType;
                        // Navin: modified on 2020/02/06
                        // storing the cardType also in the same value to avoid redoing the array positions for the rest of the values.
                        // this value has to be split into cardID and cardType while showing the values.

                        cardInformation[2] = String.valueOf(file7Contents.length / 16);  // to store the number of records in Recharge file
                        if (fileContents8.equals("")) {
                            cardInformation[3] = "0";  // to store the number of records in Recharge file
                        } else {
                            cardInformation[3] = String.valueOf(file8Contents.length / 16);  // to store the number of records in Transaction file
                        }

                        stringBuilder = new StringBuilder();

                        Log.d("fileContents10 ", "F7Length: " + file7Contents.length);
                        if (file7Contents.length > 16) {

                            stringBuilder.append(file7Contents[5]);
                            stringBuilder.append(file7Contents[4]);
                            stringBuilder.append(file7Contents[3]);
                            stringBuilder.append(file7Contents[2]);
                            stringBuilder.append(file7Contents[8]);
                            stringBuilder.append(file7Contents[7]);
                            stringBuilder.append(file7Contents[21]);
                            stringBuilder.append(file7Contents[20]);
                            stringBuilder.append(file7Contents[19]);
                            stringBuilder.append(file7Contents[18]);
                            stringBuilder.append(file7Contents[24]);
                            stringBuilder.append(file7Contents[23]);
                        } else {
                            stringBuilder.append("00 00 00 00 00 00 ");
                            stringBuilder.append(file7Contents[5]);
                            stringBuilder.append(file7Contents[4]);
                            stringBuilder.append(file7Contents[3]);
                            stringBuilder.append(file7Contents[2]);
                            stringBuilder.append(file7Contents[8]);
                            stringBuilder.append(file7Contents[7]);
                        }

                        SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy | hh:mm a");
                        Log.d("fileContents10 ", ":ss 12 to 20: " + ((stringBuilder.toString().replace(" ", "")).substring(12, 20)));
                        Log.d("fileContents10 ", ":ss 20 to 24: " + ((stringBuilder.toString().replace(" ", "")).substring(20, 24)));
                        cardInformation[4] = spf.format(CalculateDateFromHex((stringBuilder.toString().replace(" ", "")).substring(12, 20)));
                        cardInformation[5] = String.valueOf(ConvertHexToDecimal((stringBuilder.toString().replace(" ", "")).substring(20, 24)));

                        Log.d("fileContents10 ", "strbuild: " + stringBuilder);
                        Log.d("fileContents10 ", "F7C: " + file7Contents[5]);

                        if (file7Contents.length > 16) {
                            if (!file7Contents[5].equals("00 ")) {
                                cardInformation[2] = "2";  // to store the number of records in Recharge file.
                                //// If the first record value is 0, then there could be only one recharge transaction
                                cardInformation[6] = spf.format(CalculateDateFromHex((stringBuilder.toString().replace(" ", "")).substring(0, 8)));
                                cardInformation[7] = String.valueOf(ConvertHexToDecimal((stringBuilder.toString().replace(" ", "")).substring(8, 12)));
                            }
                        }

                        if (Integer.parseInt(cardInformation[3]) > 0) {
                            for (int i = 0; i < (Integer.parseInt(cardInformation[3])); i++) {
                                arrIndex = i * 16;
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(file8Contents[arrIndex + 5]);
                                stringBuilder.append(file8Contents[arrIndex + 4]);
                                stringBuilder.append(file8Contents[arrIndex + 3]);
                                stringBuilder.append(file8Contents[arrIndex + 2]);
                                stringBuilder.append(file8Contents[arrIndex + 8]);
                                stringBuilder.append(file8Contents[arrIndex + 7]);
                                stringBuilder.append(file8Contents[arrIndex + 6]);
                                stringBuilder.append(file8Contents[arrIndex + 12]);
                                stringBuilder.append(file8Contents[arrIndex + 13]);

                                arrIndex = (i * 4) + 7;     // Each transaction has 4 values. So, multiply with current loop record and add the previous 7
                                cardInformation[arrIndex + 1] = spf.format(CalculateDateFromHex((stringBuilder.toString().replace(" ", "")).substring(0, 8)));
                                cardInformation[arrIndex + 2] = FindTransactionType((stringBuilder.toString().replace(" ", "")).substring(12, 14));
                                cardInformation[arrIndex + 3] = String.valueOf(ConvertHexToDecimal((stringBuilder.toString().replace(" ", "")).substring(8, 12)));
                                cardInformation[arrIndex + 4] = FindStationName(stringBuilder.toString().replace(" ", "").substring(14, 18));
                            }
                        }








                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return cardInformation;

        }
        else {
            return null;
        }
    }

    private static String ByteArrayToString(byte[] ba)
    {
        StringBuilder hex = new StringBuilder(ba.length * 2);

        for (byte b : ba) {
            Formatter formatter = new Formatter();
            formatter.format("%02X ", b);
            hex.append(formatter.toString());
        }
        return hex.toString();
    }

    private static String[] ByteArrayToStringArray(byte[] ba)
    {
        StringBuilder hex = new StringBuilder(ba.length * 2);
        String[] strArray = new String[ba.length];
        int i = 0;

        for (byte b : ba) {
            Formatter formatter = new Formatter();
            formatter.format("%02X ", b);
            strArray[i] = formatter.toString();
            i = i + 1;
        }
        return strArray;
    }

    private static byte[] sendRequest(byte command, byte[] parameters) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        byte[] recvBuffer = isoDep.transceive(wrapMessage(command, parameters));

        while (true) {
            if (recvBuffer[recvBuffer.length - 2] != (byte) 0x91) {
                throw new Exception("Invalid response");
            }

            output.write(recvBuffer, 0, recvBuffer.length - 2);

            byte status = recvBuffer[recvBuffer.length - 1];
            if (status == OPERATION_OK) {
                break;
            } else if (status == ADDITIONAL_FRAME) {
                recvBuffer = isoDep.transceive(wrapMessage(GET_ADDITIONAL_FRAME, null));
            } else if (status == PERMISSION_DENIED) {
                throw new AccessControlException("Permission denied");
            } else if (status == AUTHENTICATION_ERROR) {
                throw new AccessControlException("Authentication error");
            } else {
                throw new Exception("Unknown status code: " + Integer.toHexString(status & 0xFF));
            }
        }

        return output.toByteArray();
    }

    private static byte[] readFile(int fileNo) throws Exception {
        return sendRequest(READ_DATA, new byte[]{
                (byte) fileNo,
                (byte) 0x0, (byte) 0x0, (byte) 0x0,
                (byte) 0x0, (byte) 0x0, (byte) 0x0
        });
    }

    private static byte[] readRecord(int fileNum) throws Exception {
        return sendRequest(READ_RECORD, new byte[]{
                (byte) fileNum,
                (byte) 0x0, (byte) 0x0, (byte) 0x0,
                (byte) 0x0, (byte) 0x0, (byte) 0x0
        });
    }

    private static String CalculateBalance(String[] file7Contents, String[] file8Contents) throws Exception {
        Date dtLastRecharge = new Date(80, 1, 1, 0, 0);
        Date dtLastTransaction = new Date(80,1,1,0,0);

        String returnValue;

        if (file7Contents.length > 1) {
            dtLastRecharge = GetDateFromFileContents(file7Contents);
        }

        if (file8Contents.length > 1) {
            dtLastTransaction = GetDateFromFileContents(file8Contents);
        }

        if (dtLastRecharge.before(dtLastTransaction)) {
            returnValue = Integer.toString(GetTrnValueFromFileContents(file8Contents));
        }
        else {
            returnValue = Integer.toString(GetTrnValueFromFileContents(file7Contents));
        }


        return returnValue;
    }

    private static String ConvertHexToBinary(String HexValue) {

        int i = Integer.parseInt(HexValue, 16);//16 bits
        String Bin = Integer.toBinaryString(i);//Converts int to binary

        String Bin2="";
        if(Bin.length()==8){Bin2=Bin;}
        if(Bin.length()==7){Bin2="0"+Bin;}
        if(Bin.length()==6){Bin2="00"+Bin;}
        if(Bin.length()==5){Bin2="000"+Bin;}
        if(Bin.length()==4){Bin2="0000"+Bin;}
        if(Bin.length()==3){Bin2="00000"+Bin;}
        if(Bin.length()==2){Bin2="000000"+Bin;}
        if(Bin.length()==1){Bin2="0000000"+Bin;}

        return  Bin2;
    }

    private static int ConvertBinaryToDecimal(String BinaryValue) {
        int i = Integer.parseInt(BinaryValue, 2);

        return i;
    }

    private static int ConvertHexToDecimal(String HexValue) {
        int i = Integer.parseInt(HexValue, 16);

        return i;
    }

    private static String FindTransactionType(String HexValue) {
        String tranType = "Unknown";

        if (HexValue.equals("1E")) {
            tranType = "Station Entry";
        } else if (HexValue.equals("28")) {
            tranType = "Station Exit";
        } else if (HexValue.equals("51")) {
            tranType = "Parking Entry";
        } else if (HexValue.equals("52")) {
            tranType = "Parking Exit";
        }
        Log.d("TranType is : ", "" + tranType + " / " + HexValue);
        Log.d("TranType", ""  + HexValue);
        Log.d("TranTypes", ""  + tranType);


        return tranType;
    }

    private static String FindStationName(String HexValue) {
        String stationName = "Unknown";

        if (HexValue.equals("0201")) {
            stationName = "Central Metro";
        } else if (HexValue.equals("0203")) {
            stationName = "Egmore Metro";
        } else if (HexValue.equals("0205")) {
            stationName = "Nehru Park";
        }else if (HexValue.equals("0207")) {
            stationName = "Kilpauk";
        }else if (HexValue.equals("0209")) {
            stationName = "Pachaiyappa";
        }else if (HexValue.equals("0211")) {
            stationName = "Shenoy Nagar";
        }else if (HexValue.equals("0213")) {
            stationName = "Anna Nagar East";
        }else if (HexValue.equals("0215")) {
            stationName = "Anna Nagar Tower";
        }else if (HexValue.equals("0217")) {
            stationName = "Thirumangalam";
        }else if (HexValue.equals("0219")) {
            stationName = "Koyambedu";
        }else if (HexValue.equals("0221")) {
            stationName = "CMBT";
        }else if (HexValue.equals("0223")) {
            stationName = "Arumbakkam";
        }else if (HexValue.equals("0225")) {
            stationName = "Vadapalani";
        }else if (HexValue.equals("0227")) {
            stationName = "Ashok Nagar";
        }else if (HexValue.equals("0229")) {
            stationName = "Ekkattuthangal";
        }else if (HexValue.equals("0231")) {
            stationName = "Alandur";
        }else if (HexValue.equals("0233")) {
            stationName = "St.Thomas Mount";
        }else if (HexValue.equals("0133")) {
            stationName = "Airport";
        }else if (HexValue.equals("0131")) {
            stationName = "Meenambakkam";
        }else if (HexValue.equals("0129")) {
            stationName = "Nanganallur Road";
        }else if (HexValue.equals("0125")) {
            stationName = "Guindy";
        }else if (HexValue.equals("0123")) {
            stationName = "Little Mount";
        }else if (HexValue.equals("0121")) {
            stationName = "Saidapet Metro";
        }else if (HexValue.equals("0119")) {
            stationName = "Nandanam";
        }else if (HexValue.equals("0117")) {
            stationName = "Teynampet";
        }else if (HexValue.equals("0115")) {
            stationName = "AG - DMS";
        }else if (HexValue.equals("0113")) {
            stationName = "Thousand Lights";
        }else if (HexValue.equals("0111")) {
            stationName = "LIC";
        }else if (HexValue.equals("0109")) {
            stationName = "Government Estate";
        }else if (HexValue.equals("0105")) {
            stationName = "High Court";
        }else if (HexValue.equals("0103")) {
            stationName = "Mannadi";
        }else if (HexValue.equals("0101")) {
            stationName = "Washermenpet";
        }

        return stationName;
    }

    private static String ConvertBinaryToHex(byte[] BinaryValue) {
        return String.format("%0" + (BinaryValue.length * 2) + "X", new BigInteger(1,BinaryValue));
    }

    private static Date GetDateFromFileContents(String[] fileContents) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(fileContents[fileContents.length - 11]);
        strBuilder.append(fileContents[fileContents.length - 12]);
        strBuilder.append(fileContents[fileContents.length - 13]);
        strBuilder.append(fileContents[fileContents.length - 14]);
        return CalculateDateFromHex(strBuilder.toString().replace(" ", ""));
    }

    private static Date CalculateDateFromHex(String dtValue) {
        String dateBinaryValue = "";
        StringBuilder strBuilder = new StringBuilder();
        char[] tranDate = new char[0];
        String[] dateBinary = new String[0];

        int dtYear, dtMonth, dtDay, dtHour, dtMinute;
        Calendar dt = Calendar.getInstance();

        tranDate = dtValue.toCharArray();
        strBuilder.append(ConvertHexToBinary(Character.toString(tranDate[0])).substring(4, 8));
        strBuilder.append(ConvertHexToBinary(Character.toString(tranDate[1])).substring(4, 8));
        strBuilder.append(ConvertHexToBinary(Character.toString(tranDate[2])).substring(4, 8));
        strBuilder.append(ConvertHexToBinary(Character.toString(tranDate[3])).substring(4, 8));
        strBuilder.append(ConvertHexToBinary(Character.toString(tranDate[4])).substring(4, 8));
        strBuilder.append(ConvertHexToBinary(Character.toString(tranDate[5])).substring(4, 8));
        strBuilder.append(ConvertHexToBinary(Character.toString(tranDate[6])).substring(4, 8));
        strBuilder.append(ConvertHexToBinary(Character.toString(tranDate[7])).substring(4, 8));

        dateBinaryValue = strBuilder.toString();

        dtYear = ConvertBinaryToDecimal(dateBinaryValue.substring(0, 7)) + 2000;
        dtMonth = ConvertBinaryToDecimal(dateBinaryValue.substring(7, 11));
        dtDay = ConvertBinaryToDecimal(dateBinaryValue.substring(11, 16));
        dtHour = ConvertBinaryToDecimal(dateBinaryValue.substring(16, 21));
        dtMinute = ConvertBinaryToDecimal(dateBinaryValue.substring(21, 27));

        dt.set(Calendar.YEAR, dtYear);
        dt.set(Calendar.MONTH, dtMonth - 1);
        dt.set(Calendar.DAY_OF_MONTH, dtDay);
        dt.set(Calendar.HOUR_OF_DAY, dtHour);
        dt.set(Calendar.MINUTE, dtMinute);
        dt.set(Calendar.SECOND, 0);
        dt.set(Calendar.MILLISECOND, 0);

        return dt.getTime();
    }

    private static int GetTrnValueFromFileContents(String[] fileContents) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(fileContents[fileContents.length - 6]);
        strBuilder.append(fileContents[fileContents.length - 7]);
        return ConvertHexToDecimal(strBuilder.toString().replace(" ", ""));
    }

    private static String GetTypeOfTicket(String ticketValue) {
        if (ticketValue.equals("08") || ticketValue.equals("09") ) {
            return "Value";
        } else if (ticketValue.equals("0A") || ticketValue.equals("0B") || ticketValue.equals("0C") || ticketValue.equals("0D") || ticketValue.equals("0E") || ticketValue.equals("0F") ) {
            return "Trip";
        } else {
            return "Unsupported";
        }

    }




}

