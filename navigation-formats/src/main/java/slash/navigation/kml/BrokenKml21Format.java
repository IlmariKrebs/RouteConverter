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

package slash.navigation.kml;

import slash.common.io.CompactCalendar;
import slash.navigation.kml.binding21.KmlType;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Logger;

/**
 * Reads broken Google Earth 4 (.kml) files.
 *
 * @author Christian Pesch
 */

public class BrokenKml21Format extends Kml21Format {
    private static final Logger log = Logger.getLogger(BrokenKml21Format.class.getName());

    public String getName() {
        return "Google Earth 4 Garble (*" + getExtension() + ")";
    }

    public boolean isSupportsWriting() {
        return false;
    }

    List<KmlRoute> internalRead(InputStream source, CompactCalendar startDate) throws IOException, JAXBException {
        InputStreamReader reader = new InputStreamReader(source);
        try {
            KmlType kmlType = KmlUtil.unmarshal21(reader);
            return process(kmlType, startDate);
        } catch (JAXBException e) {
            log.fine("Error reading broken KML 2.1 from " + source + ": " + e.getMessage());
        }
        finally {
            reader.close();
        }
        return null;
    }
}
