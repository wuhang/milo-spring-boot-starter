package com.kangaroohy.milo.runner;

import com.kangaroohy.milo.model.SubNode;
import com.kangaroohy.milo.utils.CustomUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author kangaroo hy
 * @version 0.0.1
 * @since 2020/4/14
 */
@Slf4j
public class BrowseNodeRunner {
    /**
     * 要读的节点
     */
    private final String browseRoot;

    public BrowseNodeRunner(String browseRoot) {
        this.browseRoot = browseRoot;
    }

    public List<SubNode> run(OpcUaClient opcUaClient) {
        NodeId nodeId = CustomUtil.parseNodeId(browseRoot);
        return browseNode(browseRoot, opcUaClient, nodeId);
    }

    private List<SubNode> browseNode(String prefix, OpcUaClient client, NodeId browseRoot) {
        List<SubNode> nodesList = new ArrayList<>();
        try {
            List<? extends UaNode> nodes = client.getAddressSpace().browseNodes(browseRoot);

            nodes = nodes.stream().filter(item -> !Objects.requireNonNull(item.getBrowseName().getName()).startsWith("_")).collect(Collectors.toList());

            for (UaNode node : nodes) {
                String sub = prefix + "." + node.getBrowseName().getName();

                // recursively browse to children
                List<SubNode> browseNode = browseNode(sub, client, node.getNodeId());
                if (browseNode.isEmpty()) {
                    SubNode subNode = new SubNode(
                            client,
                            node.getNodeId(),
                            node.getNodeClass(),
                            node.getBrowseName(),
                            node.getDisplayName(),
                            sub,
                            node.getDescription(),
                            node.getWriteMask(),
                            node.getUserWriteMask()
                    );
                    nodesList.add(subNode);
                } else {
                    nodesList.addAll(browseNode(sub, client, node.getNodeId()));
                }
            }
        } catch (UaException e) {
            log.error("Browsing nodeId={} failed: {}", browseRoot, e.getMessage(), e);
        }
        return nodesList;
    }
}
