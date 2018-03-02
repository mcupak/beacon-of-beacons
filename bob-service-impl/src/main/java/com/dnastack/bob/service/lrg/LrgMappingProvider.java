/* 
 * Copyright 2018 DNAstack.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dnastack.bob.service.lrg;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

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

                LrgCoordinates otherCoords = new LrgCoordinates(targetAssembly,
                                                                otherName,
                                                                otherStart,
                                                                otherEnd,
                                                                otherIsPositiveStrand);

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
        } catch (MalformedURLException | XmlRpcException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
