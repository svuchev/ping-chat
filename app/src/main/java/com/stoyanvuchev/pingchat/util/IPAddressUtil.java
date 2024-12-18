package com.stoyanvuchev.pingchat.util;

import android.util.Log;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Objects;

public class IPAddressUtil {

    public static String getLocalIPv4Address() {
        try {

            // Get all network interfaces.
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {

                NetworkInterface networkInterface = interfaces.nextElement();

                // Skip loopback and inactive interfaces.
                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }

                // Get all IP addresses for the interface.
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {

                    InetAddress inetAddress = addresses.nextElement();

                    // Check if it's an IPv4 address and not a loopback address.
                    if (inetAddress instanceof java.net.Inet4Address
                    && !(inetAddress.isLoopbackAddress())) {
                        return inetAddress.getHostAddress();
                    }

                }

            }

        } catch (Exception e) {
            Log.e(
                    "IPAddressUtil: getLocalIPv4Address",
                    Objects.requireNonNull(e.getLocalizedMessage())
            );
        }
        return null;
    }

}
