package com.richard.demo.utils.powermock;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import lombok.Data;

@Data
public class ServiceDetailDto implements Serializable {

    private static final long serialVersionUID = -3572520885116699187L;

    private String id;

    private String name;

    private String displayName;

    private String description;

    private String groupId;

    private Boolean enabled;

    private Boolean deprecated;

    private Timestamp modifiedDateTime;

    private Timestamp createdDateTime;


    private Set<String> tags;

    private String groupPath;

    private Boolean edgeAvailable;

    private String edgeUrl;

    private String webServerId;

    private String productVersion;
}
