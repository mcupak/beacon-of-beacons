/*
 * The MIT License
 *
 * Copyright 2014 Miroslav Cupak (mirocupak@gmail.com).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.dnastack.bob.lrg;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 * Service processing mapping based on lrg-sequence.org.
 *
 * @author mfiume
 */
public class LrgMappingProvider {

    private static final String authenticationKey = "TWFyYyBGaXVtZTo0dSVVVkVTWQ==";

    public static LrgMapping getMapping(String lrgID, String targetAssembly) {

        try {
            String server_url = "http://www.lrg-sequence.org/xmlrpc.php";

            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL(server_url));
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);

            Object[] params = new Object[]{authenticationKey, lrgID, targetAssembly};
            Object result = (Object) client.execute("lrg_service.getMapping", params);

            if (result instanceof HashMap) {
                HashMap otherMap = (HashMap) result;

                String otherName = (String) otherMap.get("other_name");
                long otherStart = Long.parseLong((String) otherMap.get("other_start"));
                long otherEnd = Long.parseLong((String) otherMap.get("other_end"));
                String otherStrandStr = (String) otherMap.get("strand");

                Boolean otherIsPositiveStrand = true;
                if (otherStrandStr != null && otherStrandStr.contains("-")) {
                    otherIsPositiveStrand = false;
                }

                LrgCoordinates otherCoords = new LrgCoordinates(targetAssembly, otherName, otherStart, otherEnd, otherIsPositiveStrand);

                Object[] mapping = (Object[]) otherMap.get("mapping_span");

                if (mapping.length > 0) {
                    HashMap lrgMap = (HashMap) mapping[0];

                    long lrgStart = Long.parseLong((String) lrgMap.get("lrg_start"));
                    long lrgEnd = Long.parseLong((String) lrgMap.get("lrg_end"));
                    String lrgStrandStr = (String) lrgMap.get("strand");

                    Boolean lrgIsPositiveStrand = true;
                    if (lrgStrandStr != null && lrgStrandStr.contains("-")) {
                        lrgIsPositiveStrand = false;
                    }

                    LrgCoordinates lrgCoords = new LrgCoordinates("lrg", lrgID, lrgStart, lrgEnd, lrgIsPositiveStrand);

                    return new LrgMapping(lrgCoords, otherCoords);
                }
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (XmlRpcException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
