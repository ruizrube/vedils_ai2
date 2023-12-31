/**
 * @license
 * Visual Blocks Editor
 *
 * Copyright 2013 Google Inc.
 * https://blockly.googlecode.com/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @fileoverview Inject Blockly's CSS synchronously.
 * @author fraser@google.com (Neil Fraser)
 */
'use strict';

/**
 * [lyn, 09/10/14] Limit size of runtime error dialog window,
 * [lyn, 10/10/13]
 * + Added CSS tags blocklyFieldParameter and blocklyFieldParameterFlydown
 *   to control parameter flydowns.
 * + Added CSS tags blocklyFieldProcedure and blocklyFieldProcedureFlydown
 *   to control procedure flydowns.
 */

goog.provide('Blockly.Css');

goog.require('goog.cssom');


/**
 * Inject the CSS into the DOM.  This is preferable over using a regular CSS
 * file since:
 * a) It loads synchronously and doesn't force a redraw later.
 * b) It speeds up loading by not blocking on a separate HTTP transfer.
 * c) The CSS content may be made dynamic depending on init options.
 */
Blockly.Css.inject = function() {
  var text = Blockly.Css.CONTENT.join('\n');
  // Strip off any trailing slash (either Unix or Windows).
  var path = Blockly.pathToBlockly.replace(/[\\\/]$/, '');
  text = text.replace(/<<<PATH>>>/g, path);
  goog.cssom.addCssText(text);
};

/**
 * Array making up the CSS content for Blockly.
 */
Blockly.Css.CONTENT = [
  '.blocklySvg {',
  '  background-color: #fff;',
  '  border: 1px solid #ddd;',
  '  overflow: hidden;',  /* IE overflows by default. */
  '}',

  '.blocklyWidgetDiv {',
  '  position: absolute;',
  '  display: none;',
  '  z-index: 999;',
  '}',

  '.blocklyDraggable {',
    /*
      Hotspot coordinates are baked into the CUR file, but they are still
      required in the CSS due to a Chrome bug.
      http://code.google.com/p/chromium/issues/detail?id=1446
    */
  '  cursor: url(<<<PATH>>>/media/handopen.cur) 8 5, auto;',
  '}',

  '.blocklyResizeSE {',
  '  fill: #aaa;',
  '  cursor: se-resize;',
  '}',

  '.blocklyResizeSW {',
  '  fill: #aaa;',
  '  cursor: sw-resize;',
  '}',

  '.blocklyResizeLine {',
  '  stroke-width: 1;',
  '  stroke: #888;',
  '}',

  '.blocklyHighlightedConnectionPath {',
  '  stroke-width: 4px;',
  '  stroke: #fc3;',
  '  fill: none;',
  '}',

  '.blocklyPathLight {',
  '  fill: none;',
  '  stroke-width: 2;',
  '  stroke-linecap: round;',
  '}',

  '.blocklySelected>.blocklyPath {',
  '  stroke-width: 3px;',
  '  stroke: #fc3;',
  '}',

  '.blocklySelected>.blocklyPathLight {',
  '  display: none;',
  '}',

  '.badBlock>.blocklyPath {',
  '  stroke-width: 3px;',
  '  stroke: #f00;',
  '}',

  '.badBlockl>.blocklyPathLight {',
  '  display: none;',
  '}',

  '.blocklyDragging>.blocklyPath,',
  '.blocklyDragging>.blocklyPathLight {',
  '  fill-opacity: .8;',
  '  stroke-opacity: .8;',
  '}',

  '.blocklyDragging>.blocklyPathDark {',
  '  display: none;',
  '}',

  '.blocklyDisabled>.blocklyPath {',
  '  fill-opacity: .5;',
  '  stroke-opacity: .5;',
  '}',

  '.blocklyDisabled>.blocklyPathLight,',
  '.blocklyDisabled>.blocklyPathDark {',
  '  display: none;',
  '}',

  '.blocklyText {',
  '  cursor: default;',
  '  font-family: sans-serif;',
  '  font-size: 11pt;',
  '  fill: #fff;',
  '}',

  '.blocklyNonEditableText>text {',
  '  pointer-events: none;',
  '}',

  '.blocklyNonEditableText>rect,',
  '.blocklyEditableText>rect {',
  '  fill: #fff;',
  '  fill-opacity: .6;',
  '}',

  '.blocklyNonEditableText>text,',
  '.blocklyEditableText>text {',
  '  fill: #000;',
  '}',

  '.blocklyEditableText:hover>rect {',
  '  stroke-width: 2;',
  '  stroke: #fff;',
  '}',
  '/*',
  ' * [lyn, 10/08/13] Control parameter fields with flydown getter/setter blocks.',
  ' * Brightening factors for variable color rgb(208,95,45):',
  ' * 10%: rgb(212, 111, 66)',
  ' * 20%: rgb(217, 127, 87)',
  ' * 30%: rgb(222, 143, 108)',
  ' * 40%: rgb(226, 159, 129)',
  ' * 50%: rgb(231, 175, 150)',
  ' * 60%: rgb(236, 191, 171)',
  ' * 70%: rgb(240, 207, 192)',
  ' * 80%: rgb(245, 223, 213)',
  ' * 90%: rgb(250, 239, 234)',
  ' */',
  '.blocklyFieldParameter>rect {',
  '  /* fill: rgb(231,175,150);*/ /* This looks too much like getter/setter var */',
  '  fill: rgb(222, 143, 108);',
  '  fill-opacity: 1.0;',
  '  stroke-width: 2;',
  '  stroke: rgb(231, 175, 150);',
  '}',
  '.blocklyFieldParameter>text {',
  ' /* fill: #000; */ /* Use white rather than black on dark orange */',
  '  stroke-width: 1;',
  '  fill: #000;',
  '}',
  '.blocklyFieldParameter:hover>rect {',
  '  stroke-width: 2;',
  '  stroke: rgb(231,175,150);',
  '  fill: rgb(231,175,150);',
  '  fill-opacity: 1.0;',
  '}',
  '/*',
  ' * [lyn, 10/08/13] Control flydown with the getter/setter blocks.',
  ' */',
  '.blocklyFieldParameterFlydown {',
  '  fill: rgb(231,175,150);',
  '  fill-opacity: 0.8;',
  '}',
  '/*',
  ' * [lyn, 10/08/13] Control parameter fields with flydown procedure caller block.',
  ' */',
  '.blocklyFieldProcedure>rect {',
  '  /*  rgb(231,175,150) is procedure color rgb(124,83,133) brightened by 70% */',
  '  fill: rgb(215,203,218);',
  '  fill-opacity: 1.0;',
  '  stroke-width: 0;',
  '  stroke: #000;',
  '}',
  '.blocklyFieldProcedure>text {',
  '  fill: #000;',
  '}',
  '.blocklyFieldProcedure:hover>rect {',
  '  stroke-width: 2;',
  '  stroke: #fff;',
  '  fill: rgb(215,203,218);',
  '  fill-opacity: 1.0;',
  '}',
  '/*',
  ' * [lyn, 10/08/13] Control flydown with the procedure caller block.',
  ' */',
  '.blocklyFieldProcedureFlydown {',
  '  fill: rgb(215,203,218);',
  '  fill-opacity: 0.8;',
  '}',
  '/*',
  ' * Don\'t allow users to select text.  It gets annoying when trying to',
  ' * drag a block and selected text moves instead.',
  ' */',

  '.blocklyBubbleText {',
  '  fill: #000;',
  '}',

  '/*',
  ' * [lyn, 09/10/14] Limit size of runtime error dialog window',
  ' */',
  '.blocklyRuntimeErrorDialog {',
  '  max-width: 75%;',
  '  max-height: 75%;',
  '  overflow: auto',
  '}',

  /*
    Don't allow users to select text.  It gets annoying when trying to
    drag a block and selected text moves instead.
  */
  '.blocklySvg text {',
  '  -moz-user-select: none;',
  '  -webkit-user-select: none;',
  '  user-select: none;',
  '  cursor: inherit;',
  '}',
  '/*',
  ' * Selecting text for Errors and Warnings is allowed though.',
  ' */',
  '.blocklySvg text.blocklyErrorWarningText {',
  '  -moz-user-select: text;',
  '  -webkit-user-select: text;',
  '  user-select: text;',
  '}',
  '.blocklyHidden {',
  '  display: none;',
  '}',

  '.blocklyFieldDropdown:not(.blocklyHidden) {',
  '  display: block;',
  '}',

  '.blocklyTooltipBackground {',
  '  fill: #ffffc7;',
  '  stroke-width: 1px;',
  '  stroke: #d8d8d8;',
  '}',

  '.blocklyTooltipShadow,',
  '.blocklyDropdownMenuShadow {',
  '  fill: #bbb;',
  '  filter: url(#blocklyShadowFilter);',
  '}',

  '.blocklyTooltipText {',
  '  font-family: sans-serif;',
  '  font-size: 9pt;',
  '  fill: #000;',
  '}',

  '.blocklyIconShield {',
  '  cursor: default;',
  '  fill: #00c;',
  '  stroke-width: 1px;',
  '  stroke: #ccc;',
  '}',

  '.blocklyIconGroup:hover>.blocklyIconShield {',
  '  fill: #00f;',
  '  stroke: #fff;',
  '}',

  '.blocklyIconGroup:hover>.blocklyIconMark {',
  '  fill: #fff;',
  '}',

  '.blocklyIconMark {',
  '  cursor: default !important;',
  '  font-family: sans-serif;',
  '  font-size: 9pt;',
  '  font-weight: bold;',
  '  fill: #ccc;',
  '  text-anchor: middle;',
  '}',

  '.blocklyWarningBody {',
  '}',

  '.blocklyMinimalBody {',
  '  margin: 0;',
  '  padding: 0;',
  '}',

  '.blocklyCommentTextarea {',
  '  margin: 0;',
  '  padding: 2px;',
  '  border: 0;',
  '  resize: none;',
  '  background-color: #ffc;',
  '}',

  '.blocklyHtmlInput {',
  '  font-family: sans-serif;',
  '  font-size: 11pt;',
  '  border: none;',
  '  outline: none;',
  '  width: 100%',
  '}',
  '.blocklyContextMenuBackground,',
  '.blocklyMutatorBackground {',
  '  fill: #fff;',
  '  stroke-width: 1;',
  '  stroke: #ddd;',
  '}',
  '.blocklyContextMenuOptions>.blocklyMenuDiv,',
  '.blocklyContextMenuOptions>.blocklyMenuDivDisabled,',
  '.blocklyDropdownMenuOptions>.blocklyMenuDiv {',
  '  fill: #fff;',
  '}',
  '.blocklyContextMenuOptions>.blocklyMenuDiv:hover>rect,',
  '.blocklyDropdownMenuOptions>.blocklyMenuDiv:hover>rect {',
  '  fill: #57e;',
  '}',
  '.blocklyMenuSelected>rect {',
  '  fill: #57e;',
  '}',
  '.blocklyMenuText {',
  '  cursor: default !important;',
  '  font-family: sans-serif;',
  '  font-size: 15px; /* All context menu sizes are based on pixels. */',
  '  fill: #000;',
  '}',
  '.blocklyContextMenuOptions>.blocklyMenuDiv:hover>.blocklyMenuText,',
  '.blocklyDropdownMenuOptions>.blocklyMenuDiv:hover>.blocklyMenuText {',
  '  fill: #fff;',
  '}',
  '.blocklyMenuSelected>.blocklyMenuText {',
  '  fill: #fff;',
  '}',
  '.blocklyMenuDivDisabled>.blocklyMenuText {',
  '  fill: #ccc;',
  '}',
  '.blocklyFlyoutBackground {',
  '  fill: #ddd;',
  '  fill-opacity: .8;',
  '}',
  '.blocklybackpackFlyoutBackground {',
  '  fill: #ddd;',
  '  fill-opacity: .8;',
  '}',
  '.blocklyColourBackground {',
  '  fill: #666;',
  '}',

  '.blocklyScrollbarBackground {',
  '  fill: #fff;',
  '  stroke-width: 1;',
  '  stroke: #e4e4e4;',
  '}',

  '.blocklyScrollbarKnob {',
  '  fill: #ccc;',
  '}',

  '.blocklyScrollbarBackground:hover+.blocklyScrollbarKnob,',
  '.blocklyScrollbarKnob:hover {',
  '  fill: #bbb;',
  '}',

  '.blocklyInvalidInput {',
  '  background: #faa;',
  '}',

  '.blocklyAngleCircle {',
  '  stroke: #444;',
  '  stroke-width: 1;',
  '  fill: #ddd;',
  '  fill-opacity: .8;',
  '}',

  '.blocklyAngleMarks {',
  '  stroke: #444;',
  '  stroke-width: 1;',
  '}',

  '.blocklyAngleGauge {',
  '  fill: #f88;',
  '  fill-opacity: .8;  ',
  '}',

  '.blocklyAngleLine {',
  '  stroke: #f00;',
  '  stroke-width: 2;',
  '  stroke-linecap: round;',
  '}',

  '.blocklyContextMenu {',
  '  border-radius: 4px;',
  '}',

  '.blocklyDropdownMenu {',
  '  padding: 0 !important;',
  '}',

  /* Override the default Closure URL. */
  '.blocklyWidgetDiv .goog-option-selected .goog-menuitem-checkbox,',
  '.blocklyWidgetDiv .goog-option-selected .goog-menuitem-icon {',
  '  background: url(<<<PATH>>>/media/sprites.png) no-repeat -48px -16px !important;',
  '}',

  /* Category tree in Toolbox. */
  '.blocklyToolboxDiv {',
  '  background-color: #ddd;',
  '  display: none;',
  '  overflow-x: visible;',
  '  overflow-y: auto;',
  '  position: absolute;',
  '}',

  '.blocklyTreeRoot {',
  '  padding: 4px 0;',
  '}',

  '.blocklyTreeRoot:focus {',
  '  outline: none;',
  '}',

  '.blocklyTreeRow {',
  '  line-height: 22px;',
  '  height: 22px;',
  '  padding-right: 1em;',
  '  white-space: nowrap;',
  '}',

  '.blocklyToolboxDiv[dir="RTL"] .blocklyTreeRow {',
  '  padding-right: 0;',
  '  padding-left: 1em !important;',
  '}',

  '.blocklyTreeRow:hover {',
  '  background-color: #e4e4e4;',
  '}',

  '.blocklyTreeIcon {',
  '  height: 16px;',
  '  width: 16px;',
  '  vertical-align: middle;',
  '  background-image: url(<<<PATH>>>/media/sprites.png);',
  '}',

  '.blocklyTreeIconClosedLtr {',
  '  background-position: -32px -1px;',
  '}',

  '.blocklyTreeIconClosedRtl {',
  '  background-position: 0px -1px;',
  '}',

  '.blocklyTreeIconOpen {',
  '  background-position: -16px -1px;',
  '}',

  '.blocklyTreeSelected>.blocklyTreeIconClosedLtr {',
  '  background-position: -32px -17px;',
  '}',

  '.blocklyTreeSelected>.blocklyTreeIconClosedRtl {',
  '  background-position: 0px -17px;',
  '}',

  '.blocklyTreeSelected>.blocklyTreeIconOpen {',
  '  background-position: -16px -17px;',
  '}',

  '.blocklyTreeIconNone,',
  '.blocklyTreeSelected>.blocklyTreeIconNone {',
  '  background-position: -48px -1px;',
  '}',

  '.blocklyTreeLabel {',
  '  cursor: default;',
  '  font-family: sans-serif;',
  '  font-size: 16px;',
  '  padding: 0 3px;',
  '  vertical-align: middle;',
  '}',

  '.blocklyTreeSelected  {',
  '  background-color: #57e !important;',
  '}',

  '.blocklyTreeSelected .blocklyTreeLabel {',
  '  color: #fff;',
  '}',

  /* Copied from: goog/css/colorpicker-simplegrid.css */
  /*
   * Copyright 2007 The Closure Library Authors. All Rights Reserved.
   *
   * Use of this source code is governed by the Apache License, Version 2.0.
   * See the COPYING file for details.
   */

  /* Author: pupius@google.com (Daniel Pupius) */

  /*
    Styles to make the colorpicker look like the old gmail color picker
    NOTE: without CSS scoping this will override styles defined in palette.css
  */
  '.blocklyWidgetDiv .goog-palette {',
  '  outline: none;',
  '  cursor: default;',
  '}',

  '.blocklyWidgetDiv .goog-palette-table {',
  '  border: 1px solid #666;',
  '  border-collapse: collapse;',
  '}',

  '.blocklyWidgetDiv .goog-palette-cell {',
  '  height: 13px;',
  '  width: 15px;',
  '  margin: 0;',
  '  border: 0;',
  '  text-align: center;',
  '  vertical-align: middle;',
  '  border-right: 1px solid #666;',
  '  font-size: 1px;',
  '}',

  '.blocklyWidgetDiv .goog-palette-colorswatch {',
  '  position: relative;',
  '  height: 13px;',
  '  width: 15px;',
  '  border: 1px solid #666;',
  '}',

  '.blocklyWidgetDiv .goog-palette-cell-hover .goog-palette-colorswatch {',
  '  border: 1px solid #FFF;',
  '}',

  '.blocklyWidgetDiv .goog-palette-cell-selected .goog-palette-colorswatch {',
  '  border: 1px solid #000;',
  '  color: #fff;',
  '}',

  /* Copied from: goog/css/menu.css */
  /*
   * Copyright 2009 The Closure Library Authors. All Rights Reserved.
   *
   * Use of this source code is governed by the Apache License, Version 2.0.
   * See the COPYING file for details.
   */

  /**
   * Standard styling for menus created by goog.ui.MenuRenderer.
   *
   * @author attila@google.com (Attila Bodis)
   */

  '.blocklyWidgetDiv .goog-menu {',
  '  background: #fff;',
  '  border-color: #ccc #666 #666 #ccc;',
  '  border-style: solid;',
  '  border-width: 1px;',
  '  cursor: default;',
  '  font: normal 13px Arial, sans-serif;',
  '  margin: 0;',
  '  outline: none;',
  '  padding: 4px 0;',
  '  position: absolute;',
  '  z-index: 20000;',  /* Arbitrary, but some apps depend on it... */
  '}',

  /* Copied from: goog/css/menuitem.css */
  /*
   * Copyright 2009 The Closure Library Authors. All Rights Reserved.
   *
   * Use of this source code is governed by the Apache License, Version 2.0.
   * See the COPYING file for details.
   */

  /**
   * Standard styling for menus created by goog.ui.MenuItemRenderer.
   *
   * @author attila@google.com (Attila Bodis)
   */

  /**
   * State: resting.
   *
   * NOTE(mleibman,chrishenry):
   * The RTL support in Closure is provided via two mechanisms -- "rtl" CSS
   * classes and BiDi flipping done by the CSS compiler.  Closure supports RTL
   * with or without the use of the CSS compiler.  In order for them not
   * to conflict with each other, the "rtl" CSS classes need to have the #noflip
   * annotation.  The non-rtl counterparts should ideally have them as well, but,
   * since .goog-menuitem existed without .goog-menuitem-rtl for so long before
   * being added, there is a risk of people having templates where they are not
   * rendering the .goog-menuitem-rtl class when in RTL and instead rely solely
   * on the BiDi flipping by the CSS compiler.  That's why we're not adding the
   * #noflip to .goog-menuitem.
   */
  '.blocklyWidgetDiv .goog-menuitem {',
  '  color: #000;',
  '  font: normal 13px Arial, sans-serif;',
  '  list-style: none;',
  '  margin: 0;',
     /* 28px on the left for icon or checkbox; 7em on the right for shortcut. */
  '  padding: 4px 7em 4px 28px;',
  '  white-space: nowrap;',
  '}',

  /* BiDi override for the resting state. */
  /* #noflip */
  '.blocklyWidgetDiv .goog-menuitem.goog-menuitem-rtl {',
     /* Flip left/right padding for BiDi. */
  '  padding-left: 7em;',
  '  padding-right: 28px;',
  '}',

  /* If a menu doesn't have checkable items or items with icons, remove padding. */
  '.blocklyWidgetDiv .goog-menu-nocheckbox .goog-menuitem,',
  '.blocklyWidgetDiv .goog-menu-noicon .goog-menuitem {',
  '  padding-left: 12px;',
  '}',

  /*
   * If a menu doesn't have items with shortcuts, leave just enough room for
   * submenu arrows, if they are rendered.
   */
  '.blocklyWidgetDiv .goog-menu-noaccel .goog-menuitem {',
  '  padding-right: 20px;',
  '}',

  '.blocklyWidgetDiv .goog-menuitem-content {',
  '  color: #000;',
  '  font: normal 13px Arial, sans-serif;',
  '}',

  /* State: disabled. */
  '.blocklyWidgetDiv .goog-menuitem-disabled .goog-menuitem-accel,',
  '.blocklyWidgetDiv .goog-menuitem-disabled .goog-menuitem-content {',
  '  color: #ccc !important;',
  '}',

  '.blocklyWidgetDiv .goog-menuitem-disabled .goog-menuitem-icon {',
  '  opacity: 0.3;',
  '  -moz-opacity: 0.3;',
  '  filter: alpha(opacity=30);',
  '}',

  /* State: hover. */
  '.blocklyWidgetDiv .goog-menuitem-highlight,',
  '.blocklyWidgetDiv .goog-menuitem-hover {',
  '  background-color: #d6e9f8;',
     /* Use an explicit top and bottom border so that the selection is visible',
      * in high contrast mode. */
  '  border-color: #d6e9f8;',
  '  border-style: dotted;',
  '  border-width: 1px 0;',
  '  padding-bottom: 3px;',
  '  padding-top: 3px;',
  '}',

  /* State: selected/checked. */
  '.blocklyWidgetDiv .goog-menuitem-checkbox,',
  '.blocklyWidgetDiv .goog-menuitem-icon {',
  '  background-repeat: no-repeat;',
  '  height: 16px;',
  '  left: 6px;',
  '  position: absolute;',
  '  right: auto;',
  '  vertical-align: middle;',
  '  width: 16px;',
  '}',

  /* BiDi override for the selected/checked state. */
  /* #noflip */
  '.blocklyWidgetDiv .goog-menuitem-rtl .goog-menuitem-checkbox,',
  '.blocklyWidgetDiv .goog-menuitem-rtl .goog-menuitem-icon {',
     /* Flip left/right positioning. */
  '  left: auto;',
  '  right: 6px;',
  '}',

  '.blocklyWidgetDiv .goog-option-selected .goog-menuitem-checkbox,',
  '.blocklyWidgetDiv .goog-option-selected .goog-menuitem-icon {',
     /* Client apps may override the URL at which they serve the sprite. */
  '  background: url(//ssl.gstatic.com/editor/editortoolbar.png) no-repeat -512px 0;',
  '}',

  /* Keyboard shortcut ("accelerator") style. */
  '.blocklyWidgetDiv .goog-menuitem-accel {',
  '  color: #999;',
     /* Keyboard shortcuts are untranslated; always left-to-right. */
     /* #noflip */
  '  direction: ltr;',
  '  left: auto;',
  '  padding: 0 6px;',
  '  position: absolute;',
  '  right: 0;',
  '  text-align: right;',
  '}',

  /* BiDi override for shortcut style. */
  /* #noflip */
  '.blocklyWidgetDiv .goog-menuitem-rtl .goog-menuitem-accel {',
     /* Flip left/right positioning and text alignment. */
  '  left: 0;',
  '  right: auto;',
  '  text-align: left;',
  '}',

  /* Mnemonic styles. */
  '.blocklyWidgetDiv .goog-menuitem-mnemonic-hint {',
  '  text-decoration: underline;',
  '}',

  '.blocklyWidgetDiv .goog-menuitem-mnemonic-separator {',
  '  color: #999;',
  '  font-size: 12px;',
  '  padding-left: 4px;',
  '}',

  /* Copied from: goog/css/menuseparator.css */
  /*
   * Copyright 2009 The Closure Library Authors. All Rights Reserved.
   *
   * Use of this source code is governed by the Apache License, Version 2.0.
   * See the COPYING file for details.
   */

  /**
   * Standard styling for menus created by goog.ui.MenuSeparatorRenderer.
   *
   * @author attila@google.com (Attila Bodis)
   */

  '.blocklyWidgetDiv .goog-menuseparator {',
  '  border-top: 1px solid #ccc;',
  '  margin: 4px 0;',
  '  padding: 0;',
  '}',

  ''
];