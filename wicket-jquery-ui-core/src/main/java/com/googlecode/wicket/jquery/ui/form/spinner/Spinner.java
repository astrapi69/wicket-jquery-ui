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
package com.googlecode.wicket.jquery.ui.form.spinner;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import com.googlecode.wicket.jquery.ui.IJQueryWidget;
import com.googlecode.wicket.jquery.ui.JQueryBehavior;
import com.googlecode.wicket.jquery.ui.Options;

/**
 * Provides a jQuery spinner based on a {@link TextField}
 *
 * @param <T> the type of the model object
 * @author Sebastien Briquet - sebfz1
 */
public class Spinner<T extends Number> extends TextField<T> implements IJQueryWidget
{
	private static final long serialVersionUID = 1L;
	private static final String METHOD = "spinner";

	private Options options;
//	private IConverter<T> converter;

	/**
	 * Constructor
	 * @param id the markup id
	 */
	public Spinner(final String id)
	{
		this(id, new Options(), null);
	}

	/**
	 * Constructor
	 * @param id the markup id
	 * @param options {@link Options}
	 */
	public Spinner(String id, Options options)
	{
		this(id, options, null);
	}

	/**
	 * Constructor
	 * @param id the markup id
	 * @param type type for field validation
	 */
	public Spinner(final String id, final Class<T> type)
	{
		this(id, new Options(), type);
	}

	/**
	 * Constructor
	 * @param id the markup id
	 * @param options {@link Options}
	 * @param type Type for field validation
	 */
	public Spinner(final String id, Options options, final Class<T> type)
	{
		super(id, type);

		this.options = options;
	}

	/**
	 * Constructor
	 * @param id the markup id
	 * @param model the {@link IModel}
	 */
	public Spinner(final String id, final IModel<T> model)
	{
		this(id, model, new Options(), null);
	}

	/**
	 * Constructor
	 * @param id the markup id
	 * @param model the {@link IModel}
	 * @param options {@link Options}
	 */
	public Spinner(String id, final IModel<T> model, Options options)
	{
		this(id, model, options, null);
	}

	/**
	 * Constructor
	 * @param id the markup id
	 * @param model the {@link IModel}
	 * @param type type for field validation
	 */
	public Spinner(final String id, final IModel<T> model, final Class<T> type)
	{
		this(id, model, new Options(), type);
	}

	/**
	 * Constructor
	 * @param id the markup id
	 * @param model the {@link IModel}
	 * @param options {@link Options}
	 * @param type Type for field validation
	 */
	public Spinner(final String id, final IModel<T> model, Options options, final Class<T> type)
	{
		super(id, model, type);

		this.options = options;
	}




//	/**
//	 * Constructor
//	 * @param id the markup id
//	 * @param pattern a <code>SimpleDateFormat</code> pattern
//	 * @param options {@link Options}
//	 */
//	public Spinner(String id, String pattern, Options options)
//	{
//		super(id, pattern);
//
//		this.options = options;
//	}

//	/**
//	 * Constructor
//	 * @param id the markup id
//	 * @param model the {@link IModel}
//	 * @param pattern a <code>SimpleDateFormat</code> pattern
//	 * @param options {@link Options}
//	 */
//	public Spinner(String id, IModel<Date> model, String pattern, Options options)
//	{
//		super(id, model, pattern);
//
//		this.options = options;
//	}

	// Methods //
//	@Override
//	public <C> IConverter<C> getConverter(final Class<C> type)
//	{
//		if (Number.class.isAssignableFrom(type))
//		{
//			if (this.converter == null)
//			{
//				this.converter = this.newConverter();
//			}
//
//			return (IConverter<C>) this.converter;
//		}
//		else
//		{
//			return super.getConverter(type);
//		}
//	}
//
//	private IConverter<T> newConverter()
//	{
//		return null;
//	}

	// Events //
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		this.add(JQueryWidget.newWidgetBehavior(this)); //cannot be in ctor as the markupId may be set manually afterward
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

	// Options //
	/**
	 * Sets the culture to use for parsing and formatting the value.
	 *
	 * @param culture the culture to be used
	 * @return this, for chaining
	 * @see https://github.com/jquery/globalize
	 */
	public Spinner<T> setCulture(final String culture)
	{
		this.options.set("culture", Options.asString(culture));

		return this;
	}

	/**
	 * Disables the spinner, if set to true.
	 *
	 * @param disabled
	 * @return this, for chaining
	 */
	public Spinner<T> setDisabled(final boolean disabled)
	{
		super.setEnabled(!disabled);
		this.options.set("disabled", disabled);

		return this;
	}

	/**
	 * Sets the min.
	 *
	 * @param min the min
	 * @return this, for chaining
	 */
	public Spinner<T> setMin(final Number min)
	{
		this.options.set("min", min);

		return this;
	}

	/**
	 * Sets the min.<br/>
	 * If Globalize is included, the min option can be passed as a string which will be parsed based on the numberFormat and culture options; otherwise it will fall back to the native parseFloat() method.
	 *
	 * @param min the min
	 * @return this, for chaining
	 * @see https://github.com/jquery/globalize
	 */
	public Spinner<T> setMin(final String min)
	{
		this.options.set("min", Options.asString(min));

		return this;
	}

	/**
	 * Sets the max.
	 *
	 * @param max the max
	 * @return this, for chaining
	 */
	public Spinner<T> setMax(final Number max)
	{
		this.options.set("max", max);

		return this;
	}

	/**
	 * Sets the max.<br/>
	 * If Globalize is included, the max option can be passed as a string which will be parsed based on the numberFormat and culture options; otherwise it will fall back to the native parseFloat() method.
	 *
	 * @param max the max
	 * @return this, for chaining
	 * @see https://github.com/jquery/globalize
	 */
	public Spinner<T> setMax(final String max)
	{
		this.options.set("max", Options.asString(max));

		return this;
	}

	/**
	 * Format of numbers passed to Globalize, if available. Most common are "n" for a decimal number and "C" for a currency value. Also see the culture option.
	 *
	 * @param format the number format
	 * @return this, for chaining
	 * @see https://github.com/jquery/globalize
	 * @see http://api.jqueryui.com/spinner/#option-culture
	 */
	public Spinner<T> setNumberFormat(final String format)
	{
		this.options.set("numberFormat", Options.asString(format));

		return this;
	}

	/**
	 * Sets the number of steps to take when paging via the pageUp/pageDown methods.
	 *
	 * @param steps the number of steps. Default is 10
	 * @return this, for chaining
	 */
	public Spinner<T> setPage(final Number steps)
	{
		this.options.set("page", steps);

		return this;
	}

	/**
	 * Sets the size of the step to take when spinning via buttons or via the stepUp()/stepDown() methods. The element's step attribute is used if it exists and the option is not explicitly set.
	 *
	 * @param size the size of the step. Default is 1
	 * @return this, for chaining
	 */
	public Spinner<T> setStep(final Number size)
	{
		this.options.set("step", size);

		return this;
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
				Spinner.this.onConfigure(this);
			}
		};
	}
}
