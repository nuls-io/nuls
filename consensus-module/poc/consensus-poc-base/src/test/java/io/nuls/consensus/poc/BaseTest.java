/*
 * *
 *  * MIT License
 *  *
 *  * Copyright (c) 2017-2018 nuls.io
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in all
 *  * copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  * SOFTWARE.
 *
 */

package io.nuls.consensus.poc;

import io.nuls.consensus.poc.customer.ConsensusBlockServiceImpl;
import io.nuls.consensus.poc.customer.ConsensusDownloadServiceImpl;
import io.nuls.kernel.lite.core.SpringLiteContext;
import io.nuls.protocol.service.BlockService;
import io.nuls.protocol.service.DownloadService;
import org.junit.BeforeClass;

/**
 * Created by ln on 2018/5/8.
 */
public class BaseTest {
    @BeforeClass
    public static void initClass() {
        try {
            BlockService blockService = SpringLiteContext.getBean(BlockService.class);
        } catch (Exception e) {
            SpringLiteContext.putBean(ConsensusBlockServiceImpl.class, false);
        }
        try {
            SpringLiteContext.getBean(DownloadService.class);
        } catch (Exception e) {
            SpringLiteContext.putBean(ConsensusDownloadServiceImpl.class, false);
        }
    }
}