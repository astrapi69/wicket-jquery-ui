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

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;

import com.googlecode.wicket.jquery.ui.JQueryBehavior;
import com.googlecode.wicket.jquery.ui.JQueryEvent;
import com.googlecode.wicket.jquery.ui.JQueryPanel;
import com.googlecode.wicket.jquery.ui.Options;
import com.googlecode.wicket.jquery.ui.ajax.JQueryAjaxBehavior;
import com.googlecode.wicket.jquery.ui.utils.RequestCycleUtils;

/**
 * Provides jQuery tabs based on a {@link JQueryPanel}
 *
 * @author Sebastien Briquet - sebfz1
 * @since TODO versions
 */
public class Menu extends JQueryPanel
{
	private static final long serialVersionUID = 1L;
	private static final String METHOD = "menu";

	private final Options options;
	private JQueryAjaxBehavior onClickBehavior;

	private List<MenuItem> items;
	private ListView<MenuItem> root;

	/**
	 * Constructor
	 * @param id the markup id
	 * @param items the list of {@link MenuItem}<code>s</code>
	 */
	public Menu(String id, List<MenuItem> items)
	{
		this(id, items, new Options());
	}

	/**
	 * Constructor
	 * @param id the markup id
	 * @param items the list of {@link MenuItem}<code>s</code>
	 * @param options {@link Options}
	 */
	public Menu(String id, List<MenuItem> items, Options options)
	{
		super(id);

		this.items = items; //TODO MenuModel?
		this.options = options;
		this.init();
	}

	/**
	 * Initialization
	 */
	private void init()
	{
		this.root = this.newListView("root", this.items);
		this.add(this.root);
	}

	private ListView<MenuItem> newListView(String id, List<MenuItem> items)
	{
		return new PropertyListView<MenuItem>(id, items) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<MenuItem> item)
			{
				item.add(new Label("label"));

				if (item.getModelObject().hasChild())
				{
					//item.add(newListView("children", item.getModelObject().getChildren()));
				}
				else
				{
					//item.remove("children");
				}
			}
		};
	}

	// Properties //

	// Events //
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		this.add(this.onClickBehavior = this.newOnClickBehavior());
		this.add(JQueryWidget.newWidgetBehavior(this, root));
	}

	/**
	 * Called immediately after the onConfigure method in a behavior. Since this is before the rendering
	 * cycle has begun, the behavior can modify the configuration of the component (i.e. {@link Options})
	 *
	 * @param behavior the {@link JQueryBehavior}
	 */
	protected void onConfigure(JQueryBehavior behavior)
	{
	}

	@Override
	public void onEvent(IEvent<?> event)
	{
		if (event.getPayload() instanceof ClickEvent)
		{
			ClickEvent payload = (ClickEvent) event.getPayload();

//			this.onClick(payload.getTarget(), payload.getMenuItem());
		}
	}

	// IJQueryWidget //
	@Override
	public JQueryBehavior newWidgetBehavior(String selector)
	{
		return new JQueryBehavior(selector, METHOD, this.options) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onConfigure(Component component)
			{
				Menu.this.onConfigure(this);

				this.setOption("click", onClickBehavior.getCallbackFunction());
			}
		};
	}


	// Factories //
	/**
	 * Gets a new {@link JQueryAjaxBehavior} that acts as the 'activate' javascript callback
	 * @return the {@link JQueryAjaxBehavior}
	 */
	private JQueryAjaxBehavior newOnClickBehavior()
	{
		return new JQueryAjaxBehavior(this) {

			private static final long serialVersionUID = 1L;

			@Override
			public String getCallbackFunction()
			{
				return "function(event, ui) { " + this.getCallbackScript() + " }";
			}

			@Override
			public CharSequence getCallbackScript()
			{
				return this.generateCallbackScript("wicketAjaxGet('" + this.getCallbackUrl() + "&index=' + $(event.target).tabs('option', 'active')");
			}

			@Override
			protected JQueryEvent newEvent(AjaxRequestTarget target)
			{
				return new ClickEvent(target);
			}
		};
	}


	// Event objects //
	/**
	 * Provides an event object that will be broadcasted by the {@link JQueryAjaxBehavior} 'activate' callback
	 */
	private class ClickEvent extends JQueryEvent
	{
		private final int index;

		/**
		 * Constructor
		 * @param target the {@link AjaxRequestTarget}
		 * @param step the {@link Step} (Start or Stop)
		 */
		public ClickEvent(AjaxRequestTarget target)
		{
			super(target);

			this.index = RequestCycleUtils.getQueryParameterValue("index").toInt(-1);
		}

		/**
		 * Gets the tab's index
		 * @return the index
		 */
		public int getIndex()
		{
			return this.index;
		}
	}
}
