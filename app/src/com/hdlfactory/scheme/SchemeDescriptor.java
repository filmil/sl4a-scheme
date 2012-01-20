/**
 * Copyright 2011 Filip Miletic
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.googlecode.android_scripting.Log;
import com.googlecode.android_scripting.interpreter.InterpreterConstants;
import com.googlecode.android_scripting.interpreter.Sl4aHostedInterpreter;

/**
 * A hosted interpreter that launches jScheme.
 * <p>
 * The interpreter is ran by the Dalvik VM that uses the jScheme library
 * by Ken Anderson, Tim Hickey, Peter Norvig et al.  It is ran through the 'dex'
 * utility to provide an Android-compatible jar, and then bundled in.  The original
 * library is downloadable from the
 * <a href="http://jscheme.sf.net">jScheme web site</a>.
 * <p>
 * The scripting layer for android takes care to start the interpreter up and
 * connect its standard input, output and error channel so that the user can
 * view them on the console.
 * <p>
 * The descriptor file you see here is heavily stolen from {@code BshDescriptor.java}.
 *
 * @author Filip Miletic (filmil@gmail.com)
 */
public class SchemeDescriptor extends Sl4aHostedInterpreter {

  /** Fetches the extra files from the sl4a-jscheme website. */
  private static final String MY_INSTALL_URL = "http://sl4a-scheme.googlecode.com/files/";

  private static final String JSCHEME_JAR = "jscheme-7.2.jar";

  static final String ENV_DATA = "ANDROID_DATA";

  @Override // TODO(filmil): Remove this method.
  public String getScriptsArchiveUrl() {
    return MY_INSTALL_URL + getScriptsArchiveName();
  }

  @Override // TODO(filmil): Remove this method.
  public String getExtrasArchiveUrl() {
    return MY_INSTALL_URL + getExtrasArchiveName();
  }

  @Override
  public String getName() {
    return "scheme";
  }

  @Override
  public String getNiceName() {
    return "jScheme 7.2";
  }

  @Override
  public String getExtension() {
    return ".scm";
  }

  @Override
  public int getVersion() {
    return 1;
  }

  @Override
  public boolean hasInterpreterArchive() {
    return false;
  }

  @Override
  public boolean hasExtrasArchive() {
    return true;
  }

  @Override
  public boolean hasScriptsArchive() {
    return true;
  }

  /** The binary that executes jScheme is, in fact, the Dalvik VM binary. */
  @Override
  public File getBinary(Context context) {
    return new File(DALVIKVM);
  }

  /** Wire the arguments up so as to start the main interpreter class. */
  @Override
  public List<String> getArguments(Context context) {
    File extrasPath = getExtrasPath(context);
    Log.i("Extras path: " + extrasPath);

    String absolutePathToJar = new File(extrasPath, JSCHEME_JAR).getAbsolutePath();

    List<String> result =
        new ArrayList<String>(Arrays.asList(
            "-classpath", absolutePathToJar,
            "com.android.internal.util.WithFramework", "jscheme.REPL"));
    try {
      SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
      if (preferences != null) {
        int heapsize = Integer.parseInt(preferences.getString("heapsize", "0"), 10);
        if (heapsize > 0) {
          result.add(0, String.format("-Xmx%sm", heapsize));
        }
      }
    } catch (Exception ex) {
      Log.e("While starting up:", ex);
    }
    Log.i("Running with params: " + result);
    return result;
  }

  @Override
  public Map<String, String> getEnvironmentVariables(Context unused) {
    Map<String, String> env = new HashMap<String, String>();
    env.put(ENV_DATA, InterpreterConstants.SDCARD_ROOT + getClass().getPackage().getName());
    Log.i("Using environment: " + env);
    return env;
  }
}
