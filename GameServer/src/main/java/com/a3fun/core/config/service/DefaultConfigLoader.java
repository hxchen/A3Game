package com.a3fun.core.config.service;

import com.a3fun.core.common.ConfigLoader;
import com.a3fun.core.config.model.ConfigFileBaseInfo;
import com.a3fun.core.config.model.ConfigFileInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.*;
@Slf4j
@Service
public class DefaultConfigLoader {
        private String configFileDir;
        public String getConfigFileDir() {
            return configFileDir;
        }

        @Value("${config.dir}")
        public void setConfigFileDir(String configFileDir) {
            this.configFileDir = normalizeDirPath(configFileDir);
        }
        private String normalizeDirPath(String configFileDir) {
            if (configFileDir.length() == 0) {
                return configFileDir;
            }
            char lastChar = configFileDir.charAt(configFileDir.length() - 1);
            if (lastChar == '\\' || lastChar == '/') {
                configFileDir = configFileDir.substring(0, configFileDir.length() - 1);
            }
            return configFileDir;
        }

        public ConfigFileInfo load(String configName) {
            String configPath = this.configFileDir + "/" + configName + ".json";
            InputStream in = null;
            File configFile = ConfigLoader.getConfigFile(configPath);
            if (configFile == null) {
                throw new IllegalArgumentException("Can't found config file: " + configPath);
            }

            try {
                in = new FileInputStream(configFile);
                byte[] bs = new byte[4096];
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                int n;
                while ((n = in.read(bs)) != -1) {
                    byteArrayOutputStream.write(bs, 0, n);
                }

                ConfigFileBaseInfo baseInfo = new ConfigFileBaseInfo(configName, configFile.lastModified());
                ConfigFileInfo cfi = new ConfigFileInfo(baseInfo, byteArrayOutputStream.toByteArray());
                return cfi;

            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        log.error("load config error. config path={}", configPath, e);
                    }
                }
            }
        }
}
