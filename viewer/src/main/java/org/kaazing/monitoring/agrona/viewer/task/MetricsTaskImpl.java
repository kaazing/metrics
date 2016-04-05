/**
 * Copyright 2007-2016, Kaazing Corporation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kaazing.monitoring.agrona.viewer.task;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.kaazing.monitoring.agrona.viewer.MetricsViewer;
import org.kaazing.monitoring.reader.api.Metrics;
import org.kaazing.monitoring.reader.api.Counter;
import org.kaazing.monitoring.reader.api.ServiceCounters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation for the GetMetricsTask abstraction
 * This implementation is responsible with retrieving collector messages and metrics
 * and adding them directly to the logger output.
 *
 */
public class MetricsTaskImpl implements MetricsTask {
    private String fileName;
    private ScheduledFuture<?> task;
    private static final String DEFAULT_SEPARATOR = ".";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsTaskImpl.class);

    public MetricsTaskImpl(String fileName, ScheduledExecutorService taskExecutor, Metrics reader) {
        this.fileName = fileName;
        String gatewayId = reader.getGateway().getGatewayId();
        task = taskExecutor.scheduleAtFixedRate(() -> {
                for (ServiceCounters service : reader.getServices()) {
                    for (Counter counter : service.getCounters()) {
                            String counterName =
                                    gatewayId + DEFAULT_SEPARATOR + service.getName() + DEFAULT_SEPARATOR + counter.getLabel();
                            LOGGER.debug("{} - {}", counter.getValue(), counterName);
                    }
                }
                for (Counter counter : reader.getGateway().getCounters()) {
                    String counterName = gatewayId + DEFAULT_SEPARATOR + counter.getLabel();
                    LOGGER.debug("{} - {}", counter.getValue(), counterName);
                }
            }, 0, MetricsViewer.UPDATE_INTERVAL, TimeUnit.MILLISECONDS);
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void cleanup() {
        task.cancel(true);
    }
}
