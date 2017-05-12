import itertools

import Quandl
import pandas as pd
import pdfkit
from pyparsing import Optional, oneOf, QuotedString, Word, alphanums, Combine, Literal, OneOrMore, Or, ParseException

"""
third party libs include: Quandl, pandas, pdfkit (which requires wkhtmltopdf), matplotlib and pyparsing
python env: anaconda
"""

OUT_DIR = 'e:/temp/'

TOKEN = 'please replace this'

HTML_BODY = """<html><head></head><body>{0}</body></html>"""

# define the grammar here
OUTPUT = Optional(oneOf("html pdf"), default="html")
TITLE = QuotedString('"')
SERIES_NAME = Word(alphanums)
GRAPH_ARG = Optional(oneOf("scale_log monthly"))
GRAPH = Combine(Literal('g?') + GRAPH_ARG) + Optional(OneOrMore(Literal("|") + GRAPH_ARG))
STATS_ARG = oneOf("count sum mean median min max std")
STATS = Combine(Literal('s?') + STATS_ARG) + Optional(OneOrMore(Literal("|") + STATS_ARG))
# assume this is how table operation looks like
TABLE = Word('t?')
OPERATION = Optional(Or(GRAPH | STATS | TABLE), default=GRAPH)
COMMAND = TITLE + OPERATION + Optional(OneOrMore(Literal(",") + OPERATION)) + SERIES_NAME
EXPRESSION = Optional(OUTPUT, default="html") + COMMAND + Optional(OneOrMore(Literal(";") + COMMAND))


def parse_dsl(input_str):
    def _split_list(lst, sep):
        return [list(y) for x, y in itertools.groupby(lst, lambda d: d == sep) if not x]

    try:
        instructions = EXPRESSION.parseString(input_str)
        output_format = instructions.pop(0)
        html_body = ""
        instructions = _split_list(instructions, ';')
        for instruction in instructions:
            title = instruction.pop(0)
            html_body += "<b>{0}</b><br>".format(title)
            series_name = instruction.pop(-1)
            data = query_data(series_name)
            for action in instruction:
                if action.startswith("g?"):
                    if action[2:] == "monthly":
                        data = data.groupby(pd.TimeGrouper("M")).sum()
                        html_body += img_to_html(data)
                    else:
                        html_body += img_to_html(data, "scale_log")
                elif action.startswith("s?"):
                    op = action[2:]
                    html_body += stat_to_html(data, op)
                # assume this is how table operation looks like
                elif action.startswith("t?"):
                    html_body += df_to_html(data)
        create_file(html_body, output_format)
    except ParseException:
        print "Error when parsing input!"
    pass


def query_data(series_name):
    return Quandl.get('FRED/' + series_name, authtoken=TOKEN)


def df_to_html(df):
    return df.to_html()


def img_to_html(df, option=""):
    tmp_pic_path = OUT_DIR + 'tmp.png'
    plot = df.plot(logy=option)
    fig = plot.get_figure()
    fig.savefig(tmp_pic_path, format='png')
    data_uri = open(tmp_pic_path, 'rb').read().encode('base64').replace('\n', '')
    return '<img src="data:image/png;base64,{0}">'.format(data_uri)


def stat_to_html(df, op):
    stats = getattr(df, op)().values
    return "<p>" + "<p>\n<p>".join(op + " is:" + str(m) for m in stats) + "<p>"


def create_file(body, file_format):
    """
    create a html first, then convert it to pdf if required
    """
    html = HTML_BODY.format(body)
    html_loc = OUT_DIR + 'output.html'
    with open(html_loc, 'w') as f:
        f.write(html)
    if file_format == "pdf":
        # require wkhtmltopdf
        config = pdfkit.configuration(wkhtmltopdf=r'D:\wkhtmltopdf\bin\wkhtmltopdf.exe')
        pdfkit.from_file(html_loc, OUT_DIR + 'output.pdf', configuration=config)
    print "Create output.{0} under {1}".format(file_format, OUT_DIR)


if __name__ == '__main__':
    input_dsl = raw_input()
    parse_dsl(input_dsl)
