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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.kaazing.monitoring.reader.api.Metrics;

public class MetricsTaskImplTest {

    private static final String FILE_NAME = "test";
    private Mockery context = new JUnit4Mockery() {
    };

    @Test
    public void testGetFileName() {
        ScheduledExecutorService taskExecutor = context.mock(ScheduledExecutorService.class);
        Metrics reader = context.mock(Metrics.class);
        context.checking(new Expectations() {{
            oneOf(reader).getGateway().getGatewayId();
        }});
        getScheduledTask(taskExecutor);
        MetricsTask task = new MetricsTaskImpl(FILE_NAME, taskExecutor, reader);
        assertEquals(FILE_NAME, task.getFileName());
    }

    @Test
    public void testCleanup() {
        ScheduledExecutorService taskExecutor = context.mock(ScheduledExecutorService.class);
        Metrics reader = context.mock(Metrics.class);
        context.checking(new Expectations() {{
            oneOf(reader).getGateway().getGatewayId();
        }});
        getScheduledTask(taskExecutor);
        MetricsTask task = new MetricsTaskImpl(FILE_NAME, taskExecutor, reader);
        assertNotNull(task);
        task.cleanup();
    }

    /**
     * Method returning scheduled task
     * @param taskExecutor
     * @return
     */
    private ScheduledFuture<?> getScheduledTask(ScheduledExecutorService taskExecutor) {
        ScheduledFuture<?> sched = new ScheduledFuture<Object>() {

            @Override
            public long getDelay(TimeUnit arg0) {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public int compareTo(Delayed arg0) {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public boolean cancel(boolean arg0) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public Object get() throws InterruptedException, ExecutionException {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Object get(long arg0, TimeUnit arg1) throws InterruptedException, ExecutionException, TimeoutException {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean isCancelled() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isDone() {
                // TODO Auto-generated method stub
                return false;
            }
        };
        context.checking(new Expectations() {{
            oneOf(taskExecutor).scheduleAtFixedRate(with(any(Runnable.class)), with(any(Long.class)), with(any(Long.class)), with(any(TimeUnit.class)));
            will(returnValue(sched));
        }});
        return sched;
    }
}
