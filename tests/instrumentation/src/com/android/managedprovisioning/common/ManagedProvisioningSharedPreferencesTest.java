/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.managedprovisioning.common;

import static com.android.managedprovisioning.common.ManagedProvisioningSharedPreferences.SHARED_PREFERENCE;

import static com.google.common.truth.Truth.assertThat;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.InstrumentationRegistry;
import androidx.test.filters.SmallTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SmallTest
public class ManagedProvisioningSharedPreferencesTest {

    private static final String KEY_TEST_SHARED_PREFERENCE =
            "ManagedProvisioningSharedPreferencesTest";

    @Mock
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private ManagedProvisioningSharedPreferences mManagedProvisioningSharedPreferences;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Context targetContext = InstrumentationRegistry.getTargetContext();
        mSharedPreferences = targetContext.getSharedPreferences(KEY_TEST_SHARED_PREFERENCE,
                Context.MODE_PRIVATE);
        cleanUp();

        when(mContext.getSharedPreferences(eq(SHARED_PREFERENCE), eq(Context.MODE_PRIVATE)))
                .thenReturn(mSharedPreferences);
        mManagedProvisioningSharedPreferences = new ManagedProvisioningSharedPreferences(mContext);
    }

    @After
    public void tearDown() {
        cleanUp();
    }

    private void cleanUp() {
        clearSharedPreferences();
    }

    private boolean clearSharedPreferences() {
        return mSharedPreferences.edit().clear().commit();
    }

    @Test
    public void testGetAndIncrementProvisioningId() {
        assertEquals(mManagedProvisioningSharedPreferences.incrementAndGetProvisioningId(), 1L);
        assertEquals(mManagedProvisioningSharedPreferences.getProvisioningId(), 1L);

        assertEquals(mManagedProvisioningSharedPreferences.incrementAndGetProvisioningId(), 2L);
        assertEquals(mManagedProvisioningSharedPreferences.getProvisioningId(), 2L);
    }

    @Test
    public void testProvisioningStartedTimestamp() {
        mManagedProvisioningSharedPreferences.writeProvisioningStartedTimestamp(1234);

        assertThat(mManagedProvisioningSharedPreferences.getProvisioningStartedTimestamp())
                .isEqualTo(1234);
    }

    @Test
    public void setIsProvisioningFlowDelegatedToRoleHolder_setToTrue_works() {
        mManagedProvisioningSharedPreferences.setIsProvisioningFlowDelegatedToRoleHolder(true);

        assertThat(mManagedProvisioningSharedPreferences.isProvisioningFlowDelegatedToRoleHolder())
                .isTrue();
    }

    @Test
    public void setIsProvisioningFlowDelegatedToRoleHolder_setToFalse_works() {
        mManagedProvisioningSharedPreferences.setIsProvisioningFlowDelegatedToRoleHolder(false);

        assertThat(mManagedProvisioningSharedPreferences.isProvisioningFlowDelegatedToRoleHolder())
                .isFalse();
    }

    @Test
    public void setIsProvisioningFlowDelegatedToRoleHolder_sharedPreferencesCleared_valueIsFalse() {
        clearSharedPreferences();

        assertThat(mManagedProvisioningSharedPreferences.isProvisioningFlowDelegatedToRoleHolder())
                .isFalse();
    }
}
