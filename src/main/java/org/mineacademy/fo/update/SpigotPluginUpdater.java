package org.mineacademy.fo.update;

import org.bukkit.plugin.java.JavaPlugin;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.VersionComparator;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Benjamin | Bentipa
 * @version 1.0 Created on 03.04.2015
 * <p>
 * Edit By LU__LU
 */
public class SpigotPluginUpdater {

    private URL url;
    private final JavaPlugin plugin;
    private final String pluginUrl;
    private String newestVersion = "";
    private String downloadUrlOrFileName = "";
    private String changeLog = "";

    private boolean canceled = false;

    public SpigotPluginUpdater(JavaPlugin plugin, String url) {
        this.plugin = plugin;
        this.pluginUrl = url;

        connectUrl();
    }

    private void connectUrl() {
        try {
            url = new URL(pluginUrl);
        } catch (MalformedURLException e) {
            canceled = true;
            Common.log("&cError: Bad URL while checking " + plugin.getName() + " !");
        }
    }

    public boolean needsUpdate() {
        if (canceled)
            return false;

        try {
            fetchNewVersionInfosFromUrl();

            if (newVersionAvailable()) {
                Common.log(
                        "&aNew Version found: " + newestVersion,
                        "&aChangelog: &f", changeLog);
                return true;
            }

        } catch (IOException | SAXException | ParserConfigurationException e) {
            Common.log("&cError in checking update for " + plugin.getName() + "!");
            Common.log("&cError: ", e.getMessage());
        }

        return false;
    }

    private void fetchNewVersionInfosFromUrl() throws IOException, SAXException, ParserConfigurationException {
        URLConnection con = url.openConnection();
        InputStream _in = con.getInputStream();
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(_in);

        Node nod = doc.getElementsByTagName("item").item(0);
        NodeList children = nod.getChildNodes();

        newestVersion = children.item(1).getTextContent();
        downloadUrlOrFileName = children.item(3).getTextContent();
        changeLog = children.item(5).getTextContent();
    }

    private boolean newVersionAvailable() {
        return !VersionComparator.isAtLeast(plugin.getDescription().getVersion(), this.newestVersion);
    }

    public void update() throws IOException {
        String fileName = downloadUrlOrFileName;
        String url = getFolder(pluginUrl) + fileName;

        downloadUpdateToPluginsFolder(fileName, url);
    }

    public void externalUpdate() throws IOException {
        String fileName = plugin.getName() + ".jar";
        String url = downloadUrlOrFileName;

        downloadUpdateToPluginsFolder(fileName, url);
    }

    private void downloadUpdateToPluginsFolder(String fileName, String url) throws IOException {
        URL download = new URL(url);
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            Common.log("&eTrying to download newest version of " + plugin.getName() + "...");

            in = new BufferedInputStream(download.openStream());
            fout = new FileOutputStream(getNonRepeatFileName(fileName));

            byte data[] = new byte[4096];
            int count;
            while ((count = in.read(data, 0, 4096)) != -1) {
                fout.write(data, 0, count);
            }
        } finally {
            if (in != null) {

                in.close();
            }
            if (fout != null) {
                fout.close();
            }
        }

        Common.log("&aSuccessfully downloaded the newest one!");
        Common.log("&aTo install the new features you have to restart your server!");

    }

    private String getNonRepeatFileName(String fileName) {
        File pluginsFolder = new File("plugins/");

        Set<String> pluginNames = Arrays.stream(pluginsFolder.listFiles(File::isFile))
                .map(file -> file.getName())
                .collect(Collectors.toSet());

        int number = 1;
        String newFileName = fileName;

        while (pluginNames.contains(newFileName)) {
            newFileName = "(" + number + ") " + fileName;
            number++;
        }

        return pluginsFolder.getName() + "/" + newFileName;
    }

    private String getFolder(String s) {
        return s.substring(0, s.lastIndexOf("/") + 1);
    }
}