/*
 * Copyright (c) 2017-2018 PLACTAL.
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.mithrilcoin.eos.data.remote.model.api;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

import io.mithrilcoin.eos.crypto.ec.EosPublicKey;

/**
 * Created by swapnibble on 2017-11-15.
 */

public class RequiredKeysResponse {

    @Expose
    private List<String> required_keys ;

    public List<EosPublicKey> getKeys() {
        if ( null == required_keys ){
            return new ArrayList<>();
        }

        ArrayList<EosPublicKey> retKeys = new ArrayList<>(required_keys.size());
        for ( String pubKey: required_keys ){
            retKeys.add( new EosPublicKey( pubKey));
        }

        return retKeys;
    }
}
