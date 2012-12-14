/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.wicket.jquery.ui.widget.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.IClusterable;

/**
 * TODO javadoc
 * @author Sebastien Briquet - sebfz1
 *
 */
public class MenuItem implements IClusterable
{
	private static final long serialVersionUID = 1L;

	private String label;
	private List<MenuItem> items = null;

	public MenuItem(final String label)
	{
		this.label = label;
	}

	public MenuItem(final String label, MenuItem parent)
	{
		this(label);

		parent.addChild(this);
	}

	// Properties //
	public String getLabel()
	{
		return this.label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	// Methods //
	public boolean hasChild()
	{
		return (this.items != null) && (this.items.size() > 0);
	}

	public List<MenuItem> getChildren()
	{
		if (this.items != null)
		{
			return Collections.unmodifiableList(this.items);
		}

		return Collections.emptyList();
	}

	public synchronized boolean addChild(MenuItem item)
	{
		if (this.items == null)
		{
			this.items = new ArrayList<MenuItem>();
		}

		return this.items.add(item);
	}

	public boolean removeChild(MenuItem item)
	{
		if (this.items != null)
		{
			return this.items.remove(item);
		}

		return false;
	}

}
