/*
    This file is part of RouteConverter.

    RouteConverter is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    RouteConverter is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with RouteConverter; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

    Copyright (C) 2007 Christian Pesch. All Rights Reserved.
*/

package slash.navigation.wbt;

import slash.navigation.Wgs84Route;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

/**
 * Reads and writes Wintec WBT-201 (.tk2) files.
 * <p/>
 * Format is documented at:
 * http://forum.pocketnavigation.de/attachment.php?attachmentid=1082952
 * http://forum.pocketnavigation.de/attachment.php?attachmentid=1082953
 * http://www.steffensiebert.de/soft/python/wintec_201_fileformat.html
 *
 * @author Malte Neumann
 */

public class WintecWbt201Tk2Format extends WintecWbt201Format {
    private static final String FORMAT_DESCRIPTOR = "WintecLogTk2\u0000\u0000\u0000";

    public String getExtension() {
        return ".tk2";
    }

    protected boolean checkFormatDescriptor(ByteBuffer buffer) throws IOException {
        buffer.position(0);
        String formatDescriptor = extractFormatDescriptor(buffer);
        return formatDescriptor.equals(FORMAT_DESCRIPTOR);
    }

    protected List<Wgs84Route> read(ByteBuffer source) throws IOException {
        /*
           char pHeader[16];//="WintecLogTk2";   //16
           float f32LogVersion;                  //20
           float f32SWVersion;                   //24
           float f32HWVersion;                   //28
           unsigned short u16FlashID;            //30
           char pDeviceName1[20];                //50
           char pDeviceName2[20];                //70
           char pDeviceName3[40];                //110
           char pLocalTimeOfReading[20];         //130
           //------ Tk2 New --------------------------------
           char TrackDescription[300];           //430
           char ZoneIndex;                       //431  // 1 or -1
           unsigned char ZoneHour;               //432
           unsigned char ZoneMinite;             //433
           char pTimeToFirstPoint[27];           //460
           unsigned int TotalPoint;              //464
           unsigned int TotalTime_s;             //468
           unsigned int TotalDistance_m;         //472
           unsigned int NumberOfPushToLog;       //476
           //------------------------------------------------
           char pResever1[548];                  //1024
         */

        source.order(ByteOrder.LITTLE_ENDIAN);
        source.position(0);
        String formatDescriptor = extractFormatDescriptor(source);
        /*float logVersion =*/ source.getFloat();
        /*float swVersion =*/ source.getFloat();
        /*float hwVersion =*/ source.getFloat();

        source.position(40);
        source.get(new byte[20]);

        source.position(140);
        // Tk2 has no TrackInfo Structure, set position to end of file
        // readPositions processes this correctly
        long startTrackInfoStruct = source.capacity();

        if (!formatDescriptor.equals(FORMAT_DESCRIPTOR))
            return null;

        return readPositions(source, startTrackInfoStruct);
    }
}
