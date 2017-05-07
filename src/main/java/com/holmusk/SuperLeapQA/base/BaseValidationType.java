package com.holmusk.SuperLeapQA.base;

/**
 * Created by haipham on 5/7/17.
 */

import org.swiften.xtestkit.test.BaseTestType;

/**
 * Interfaces that extend this should declare methods that assist with app
 * validation (e.g. make sure all views are present).
 */
public interface BaseValidationType extends BaseTestType, AppDelayType {}
