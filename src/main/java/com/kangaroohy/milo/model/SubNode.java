package com.kangaroohy.milo.model;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.core.nodes.Node;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;

public class SubNode extends UaNode {
    private String nodePath;

    public SubNode(
            OpcUaClient client,
            NodeId nodeId,
            NodeClass nodeClass,
            QualifiedName browseName,
            LocalizedText displayName,
            String nodePath,
            LocalizedText description,
            UInteger writeMask,
            UInteger userWriteMask
    ) {
        super(client,
                nodeId,
                nodeClass,
                browseName,
                displayName,
                description,
                writeMask,
                userWriteMask
        );
        this.setNodePath(nodePath);
    }

    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }

    public String getNodePath() {
        return nodePath;
    }
}
