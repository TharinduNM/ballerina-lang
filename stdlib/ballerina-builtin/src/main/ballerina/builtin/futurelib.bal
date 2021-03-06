// Copyright (c) 2018 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

documentation {
    Checks if the given future is done.

    R{{}} true if the future is done; false otherwise
}
public native function<future f> isDone() returns boolean;

documentation {
    Checks if the given future is cancelled.

    R{{}} true if the future is cancelled; false otherwise
}
public native function<future f> isCancelled() returns boolean;

documentation {
    Cancels the given future.

    R{{}} true if the future was cancelled; false otherwise
}
public native function<future f> cancel() returns boolean;
