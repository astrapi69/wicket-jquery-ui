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
package com.googlecode.wicket.jquery.ui.widget.accordion;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.model.Model;

import com.googlecode.wicket.jquery.ui.JQueryBehavior;
import com.googlecode.wicket.jquery.ui.JQueryEvent;
import com.googlecode.wicket.jquery.ui.JQueryPanel;
import com.googlecode.wicket.jquery.ui.Options;
import com.googlecode.wicket.jquery.ui.ajax.JQueryAjaxBehavior;
import com.googlecode.wicket.jquery.ui.utils.RequestCycleUtils;
import com.googlecode.wicket.jquery.ui.widget.tabs.AjaxTab;

/**
 * Provides a jQuery accordion based on a {@link JQueryPanel}, which takes {@link ITab}<code>s</code> as contructor's argument
 *
 * @author Sebastien Briquet - sebfz1
 * @since 1.2.3
 * @since 6.0.1
 */
public class AccordionPanel extends JQueryPanel
{
	private static final long serialVersionUID = 1L;

	private final List<ITab> tabs;
	private final Options options;
	private AccordionBehavior widgetBehavior;
	private JQueryAjaxBehavior onClickBehavior;
	private JQueryAjaxBehavior onActivateBehavior;


	/**
	 * Constructor
	 * @param id the markup id
	 * @param tabs the list of {@link ITab}
	 */
	public AccordionPanel(String id, List<ITab> tabs)
	{
		this(id, tabs, new Options());
	}

	/**
	 * Constructor
	 * @param id the markup id
	 * @param tabs the list of {@link ITab}
	 * @param options {@link Options}
	 */
	public AccordionPanel(String id, List<ITab> tabs, Options options)
	{
		super(id);

		this.tabs = tabs;
		this.options = options;

		this.init();
	}

	/**
	 * Initialization
	 */
	private void init()
	{
		this.add(new Loop("tabs", this.getCountModel()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(LoopItem item)
			{
				int index = item.getIndex();
				final ITab tab = AccordionPanel.this.tabs.get(index);

				if (tab.isVisible())
				{
					item.add(new Label("title", tab.getTitle()));
					item.add(tab.getPanel("panel"));

					if (index == this.getActiveIndex() && tab instanceof AjaxTab)
					{
						AccordionPanel.this.scheduleLoad((AjaxTab) tab);
					}
				}
			}

			/**
			 * Gets the tab's index ('active') that has been set by #onConfigure or #activate method
			 * @return the user-selected index
			 */
			private int getActiveIndex()
			{
				Integer index = (Integer) widgetBehavior.getOption("active"); //do not use Accordion.this.options here, behavior options could have been set during onConfigure

				return index != null ? index : 0;
			}
		});
	}

	/**
	 * Schedule the load of the selected ajax tab
	 */
	//can be removed if accordion widget provides a 'show' event (as for tab widget)
	private void scheduleLoad(final AjaxTab tab)
	{
		this.add(new AbstractDefaultAjaxBehavior() {

			private static final long serialVersionUID = 1L;

			@Override
			protected void respond(AjaxRequestTarget target)
			{
				tab.load(target);
			}

			@Override
			public void renderHead(Component component, IHeaderResponse response)
			{
				super.renderHead(component, response);

				response.renderOnDomReadyJavaScript(this.getCallbackScript().toString());
			}
		});
	}


	// Properties //
	/**
	 * Activates the selected tab
	 * @param index the tab's index to activate
	 * @return this, for chaining
	 */
	public AccordionPanel setActiveTab(int index)
	{
		this.options.set("active", index);

		return this;
	}

	/**
	 * Activates the selected tab
	 * @param target the {@link AjaxRequestTarget}
	 * @param index the tab's index to activate
	 */
	public void setActiveTab(int index, AjaxRequestTarget target)
	{
		this.widgetBehavior.activate(index, target); //sets 'active' option, that fires 'activate' event (best is that is also fires 'show' event)
	}

	/**
	 * Gets the model of tab's count
	 * @return the {@link Model}
	 */
	//TODO: AccordionPanel/sample: adding a tab
	private Model<Integer> getCountModel()
	{
		return new Model<Integer>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Integer getObject()
			{
				return AccordionPanel.this.tabs.size();
			}
		};
	}

	/**
	 * Indicates whether the 'click' event is enabled
	 * If true, the {@link #onClick(AjaxRequestTarget, int, ITab)} event will be triggered
	 *
	 * @return false by default
	 */
	protected boolean isOnClickEnabled()
	{
		return false;
	}

	// Events //
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		this.add(this.onClickBehavior = this.newOnClickBehavior());
		this.add(this.onActivateBehavior = this.newOnActivateBehavior());
		this.add(this.widgetBehavior = (AccordionBehavior) JQueryWidget.newWidgetBehavior(this));
	}

	@Override
	public void onEvent(IEvent<?> event)
	{
		if (event.getPayload() instanceof AccordionEvent)
		{
			AccordionEvent payload = (AccordionEvent) event.getPayload();
			AjaxRequestTarget target = payload.getTarget();

			int index = payload.getIndex();
			ITab tab = this.tabs.get(index);

			if (payload instanceof ClickEvent)
			{
				this.onClick(target, index, tab);
			}

			if (payload instanceof ActivateEvent)
			{
				// AjaxTab#load cannot be called in click block (#activate only fires 'activate' event)
				if (tab instanceof AjaxTab)
				{
					((AjaxTab)tab).load(target);
				}

				this.onActivate(target, index, tab);
			}
		}
	}

	/**
	 * Called immediately after the onConfigure method in a behavior. Since this is before the rendering
	 * cycle has begun, the behavior can modify the configuration of the component (i.e. {@link Options})
	 *
	 * @param behavior the {@link JQueryBehavior}
	 */
	protected void onConfigure(JQueryBehavior behavior)
	{
		behavior.setOptions(this.options);
	}

	/**
	 * Triggered when an accordion tab is clicked ('click' event).<br/>
	 * {@link #isOnClickEnabled()} should return true for this event to be triggered.<br/>
	 * <b>Note:</b> the click event occurs before the activate event
	 *
	 * @param target the {@link AjaxRequestTarget}
	 * @param index the accordion header that triggered this event
	 * @param tab the {@link ITab} that corresponds to the index
	 */
	//better to be replaced by #onShow() (if/when available)
	protected void onClick(AjaxRequestTarget target, int index, ITab tab)
	{
	}

	/**
	 * Triggered when an accordion tab has been activated ('activate' event).<br/>
	 * <b>Note:</b> the activate event occurs after the click event
	 *
	 * @param target the {@link AjaxRequestTarget}
	 * @param index the accordion header that triggered this event
	 * @param tab the {@link ITab} that corresponds to the index
	 */
	protected void onActivate(AjaxRequestTarget target, int index, ITab tab)
	{
	}

	// IJQueryWidget //
	@Override
	public JQueryBehavior newWidgetBehavior(String selector)
	{
		return new AccordionBehavior(selector) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onConfigure(Component component)
			{
				AccordionPanel.this.onConfigure(this);

				if (AccordionPanel.this.isOnClickEnabled())
				{
					this.on("click", onClickBehavior.getCallbackFunction());
				}

				this.setOption("activate", onActivateBehavior.getCallbackFunction());
			}
		};
	}

	// Factories //
	/**
	 * Gets a new {@link JQueryAjaxBehavior} that acts as the 'click' callback
	 * @return the {@link JQueryAjaxBehavior}
	 */
	private JQueryAjaxBehavior newOnClickBehavior()
	{
		return new JQueryAjaxBehavior(this) {

			private static final long serialVersionUID = 1L;

			@Override
			public String getCallbackFunction()
			{
				return "function(event) { " + this.getCallbackScript() + " }";
			}

			@Override
			public CharSequence getCallbackScript()
			{
				return this.generateCallbackScript("wicketAjaxGet('" + this.getCallbackUrl() + "&index=' + $(event.currentTarget).accordion('option', 'active')");
			}

			@Override
			protected JQueryEvent newEvent(AjaxRequestTarget target)
			{
				return new ClickEvent(target);
			}
		};
	}

	/**
	 * Gets a new {@link JQueryAjaxBehavior} that acts as the 'activate' callback
	 * @return the {@link JQueryAjaxBehavior}
	 */
	private JQueryAjaxBehavior newOnActivateBehavior()
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
				return this.generateCallbackScript("wicketAjaxGet('" + this.getCallbackUrl() + "&index=' + $(event.target).accordion('option', 'active')");
			}

			@Override
			protected JQueryEvent newEvent(AjaxRequestTarget target)
			{
				return new ActivateEvent(target);
			}
		};
	}

	// Event objects //
	/**
	 * Base class for accordion event objects
	 */
	abstract class AccordionEvent extends JQueryEvent
	{
		private final int index;

		/**
		 * Constructor
		 * @param target the {@link AjaxRequestTarget}
		 */
		public AccordionEvent(AjaxRequestTarget target)
		{
			super(target);

			this.index = RequestCycleUtils.getQueryParameterValue("index").toInt();
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

	/**
	 * Provides an event object that will be broadcasted by the {@link JQueryAjaxBehavior} 'click' callback
	 */
	class ClickEvent extends AccordionEvent
	{
		/**
		 * Constructor
		 * @param target the {@link AjaxRequestTarget}
		 */
		public ClickEvent(AjaxRequestTarget target)
		{
			super(target);
		}
	}

	/**
	 * Provides an event object that will be broadcasted by the {@link JQueryAjaxBehavior} 'activate' callback
	 */
	class ActivateEvent extends AccordionEvent
	{
		/**
		 * Constructor
		 * @param target the {@link AjaxRequestTarget}
		 */
		public ActivateEvent(AjaxRequestTarget target)
		{
			super(target);
		}
	}
}
