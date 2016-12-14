package com.me.core.service.importbl;

import com.me.common.Executable;
import com.me.common.ProgressWatcher;
import com.me.common.StoppableObservable;
import com.me.core.domain.dto.ConfigEntry;
import com.me.core.domain.entities.Blacklist;
import com.me.core.service.utils.Utils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@PropertySource("classpath:settings.properties")
public class BlacklistImporterService extends StoppableObservable implements Executable {

    @Getter
    @Setter
    @Value("${app.working.dir}//blacklists.json")
    private String jsonConfigPath;

    @Getter
    @Setter
    @Value("${app.working.dir}//blacklists2//uncompressed//")
    private String blacklistsPath;

    @Getter
    private final AddBehaviour addBehaviour;

    @Autowired
    public BlacklistImporterService(@Qualifier("progressWatcher") ProgressWatcher watcher,
                                    @Qualifier("fileSystemBehaviour") AddBehaviour addBehaviour) {
        super.addSubscriber(watcher);
        this.addBehaviour = addBehaviour;
    }

    public void importAll() throws Exception {
        List<ConfigEntry> blacklistsConfig = Utils.parseJson(jsonConfigPath);
        for (ConfigEntry configEntry : blacklistsConfig) {
            String filePath = blacklistsPath + "/" + configEntry.getFolderName();
            Blacklist blacklist = configEntry.getBlacklist();

            super.updateMetaCheck("doing task for: " + blacklist.getBlacklistName());
            addBehaviour.importBlacklist(blacklist, filePath);
        }
    }

    @Override
    public void afterCancel() {

    }

    @Override
    public void execute(Object... args) throws Exception {
        importAll();
    }
}
