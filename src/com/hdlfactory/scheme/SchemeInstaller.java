/**
 * Copyright 2011 Google Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hdlfactory.scheme;

import java.io.File;

import android.content.Context;

import com.googlecode.android_scripting.AsyncTaskListener;
import com.googlecode.android_scripting.InterpreterInstaller;
import com.googlecode.android_scripting.Log;
import com.googlecode.android_scripting.exception.Sl4aException;
import com.googlecode.android_scripting.interpreter.InterpreterConstants;
import com.googlecode.android_scripting.interpreter.InterpreterDescriptor;

/**
 * The bare-bones installer for the scheme interpreter.
 * <p>
 * Reuses the existing infrastructure provided by the scripting layer,
 * adding code to initialize the cache root directory.
 * <p>
 *
 * @author filmil@gmail.com (Filip Miletic)
 */
public class SchemeInstaller extends InterpreterInstaller {

  public SchemeInstaller(InterpreterDescriptor descriptor, Context context,
      AsyncTaskListener<Boolean> listener) throws Sl4aException {
    super(descriptor, context, listener);
  }

  // Requires permissions to write to the SD memory card (/sdcard/...)
  @Override
  protected boolean setup() {
    Log.i("Now installing");
    File dalvikCache = new File(mInterpreterRoot + InterpreterConstants.SL4A_DALVIK_CACHE_ROOT);
    if (!dalvikCache.isDirectory()) {
      try {
        dalvikCache.mkdir();
      } catch (SecurityException ex) {
        Log.e(mContext, "Could not make vm cache", ex);
        return false;
      }
    }
    return true;
  }
}
